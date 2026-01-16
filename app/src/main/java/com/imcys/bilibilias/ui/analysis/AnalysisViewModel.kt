package com.imcys.bilibilias.ui.analysis

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.event.AnalysisEvent
import com.imcys.bilibilias.common.event.sendAnalysisEvent
import com.imcys.bilibilias.common.event.sendToastEvent
import com.imcys.bilibilias.common.utils.AsRegexUtil
import com.imcys.bilibilias.common.utils.FirebaseExt
import com.imcys.bilibilias.common.utils.TextType
import com.imcys.bilibilias.common.utils.toHttps
import com.imcys.bilibilias.data.model.download.CCFileType
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.download.NewDownloadManager
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.app.AppOldCommonBean
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerSynthesize
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILISteinEdgeInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurls
import com.imcys.bilibilias.network.model.video.BILIVideoLanguageItem
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoSupportFormat
import com.imcys.bilibilias.network.model.video.SelectEpisodeType
import com.imcys.bilibilias.network.service.AppAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeSet

@OptIn(FlowPreview::class)
class AnalysisViewModel(
    usersDataSource: UsersDataSource,
    private val videoInfoRepository: VideoInfoRepository,
    private val userInfoRepository: UserInfoRepository,
    private val downloadManager: NewDownloadManager,
    private val appSettingsRepository: AppSettingsRepository,
    private val appAPIService: AppAPIService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalysisUIState())
    val uiState = _uiState.asStateFlow()

    val appSettings = appSettingsRepository.appSettingsFlow

    private val _donghuaPlayerInfo =
        MutableStateFlow<NetWorkResult<BILIDonghuaPlayerSynthesize?>>(emptyNetWorkResult())
    val donghuaPlayerInfo = _donghuaPlayerInfo.asStateFlow()

    private val _videoPlayerInfo =
        MutableStateFlow<NetWorkResult<BILIVideoPlayerInfo?>>(emptyNetWorkResult())
    val videoPlayerInfo = _videoPlayerInfo.asStateFlow()

    private val _interactiveVideo =
        MutableStateFlow<NetWorkResult<BILISteinEdgeInfo?>>(emptyNetWorkResult())
    val interactiveVideo = _interactiveVideo.asStateFlow()


    private val _currentUserInfo = MutableStateFlow<BILIUsersEntity?>(null)

    val currentUserInfo = _currentUserInfo.asStateFlow()

    private val _boostVideoInfo = MutableStateFlow<AppOldCommonBean?>(null)
    val boostVideoInfo = _boostVideoInfo.asStateFlow()


    private val debounceTime = 1000L // 防抖时间
    private val debounceJob: MutableStateFlow<String?> = MutableStateFlow(null)


    init {

        viewModelScope.launch(Dispatchers.IO) {
            usersDataSource.users.collect {
                val oldLoginState = _uiState.value.isBILILogin
                _uiState.value = _uiState.value.copy(
                    isBILILogin = it.currentUserId != 0L
                )
                if (oldLoginState != _uiState.value.isBILILogin) {
                    // 登录状态变化，刷新解析
                    _uiState.value.inputAsText.let { inputAsText ->
                        if (inputAsText.isNotEmpty()) {
                            analysisInputText(inputAsText)
                        }
                    }
                }

            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            debounceJob
                .debounce(debounceTime) // 设置防抖时间
                .distinctUntilChanged() // 避免重复处理相同的输入
                .collect { inputAsText ->
                    if (inputAsText.isNullOrEmpty()) return@collect
                    analysisInputText(inputAsText)
                }
        }

        viewModelScope.launch {
            _currentUserInfo.value = userInfoRepository.getCurrentUser()
        }

        viewModelScope.launch(Dispatchers.IO) {
            appSettings.collect {
                _uiState.update { it.copy(episodeListMode = it.episodeListMode) }
            }
        }

    }

    suspend fun loadBoostVideoInfo(): Result<AppOldCommonBean> {
        return appAPIService.getAppOldBoostVideoInfo()
    }

    /**
     * 下载封面
     */
    suspend fun downloadImageToAlbum(
        context: Context,
        imageUrl: String?,
        saveDirName: String
    ) = withContext(Dispatchers.IO) {
        if (imageUrl.isNullOrEmpty()) {
            sendToastEvent("图片链接不能为空")
            return@withContext
        }
        val type = imageUrl.substringAfterLast(".")
        downloadManager.downloadImageToAlbum(
            imageUrl, when (val result = _uiState.value.asLinkResultType) {
                is ASLinkResultType.BILI.Donghua -> "${result.currentEpId}_pic.${type}"
                is ASLinkResultType.BILI.Video -> "${result.viewInfo.data?.cid}_pic.${type}"
                else -> "${System.currentTimeMillis()}.${type}"
            }, saveDirName
        )
        sendToastEvent("保存成功")
    }

    /**
     * 当前仅支持解析B站视频，后续兼容AcFun
     */
    fun updateInputAsText(inputAsText: String) {
        _uiState.value = _uiState.value.copy(
            inputAsText = inputAsText,
            analysisBaseInfo = AnalysisBaseInfo(),
        )
        if (inputAsText.isEmpty()) return
        debounceJob.value = inputAsText
    }


    fun updateDownloadCover(downloadCover: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(downloadCover = downloadCover)
        )
    }

    // 清空cid列表
    fun clearSelectedCidList() {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.clearCidList()
        )
    }

    // 清空epId列表
    fun clearSelectedEpIdList() {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.clearEpIdList()
        )
    }



    fun updateSelectedPlayerInfo(
        cid: Long,
        selectEpisodeType: SelectEpisodeType,
        title: String,
        cover: String,
    ) {

        _uiState.update {
            _uiState.value.copy(
                analysisBaseInfo = AnalysisBaseInfo(
                    enabledSelectInfo = true,
                    title = title,
                    cover = cover.toHttps(),
                ),
            )
        }

        when (val result = uiState.value.asLinkResultType) {
            is ASLinkResultType.BILI.Donghua -> {

                result.donghuaViewInfo.data?.let { info ->
                    // 这里cid是ep号
                    when (selectEpisodeType) {
                        is SelectEpisodeType.AID -> {
                            sendAnalysisEvent(AnalysisEvent("av${selectEpisodeType.aid}"))
                        }

                        is SelectEpisodeType.EPID -> {
                            asDonghuaPlayerInfo(null, cid)
                        }

                        else -> {}
                    }
                }
            }

            is ASLinkResultType.BILI.Video -> {
                val viewData = result.viewInfo.data

                if (viewData?.rights?.isSteinGate != 0L) {
                    interactiveVideo.value.data?.storyList?.firstOrNull {
                        it.cid == cid
                    }?.let { story ->
                        updatePlayerInfoV2(cid = cid, bvId = viewData?.bvid, aid = viewData?.aid)
                        asVideoPlayerInfo(cid, viewData?.bvid, viewData?.aid)
                        return@let
                    }
                    return
                }

                viewData.let { info ->
                    // 分P
                    info.pages?.firstOrNull { it.cid == cid }?.let {
                        updatePlayerInfoV2(cid = cid, bvId = info.bvid, aid = info.aid)
                        asVideoPlayerInfo(cid, info.bvid)
                        return@let
                    }
                    // 合集
                    info.ugcSeason?.sections?.forEach { section ->
                        section.episodes.forEach { episode ->
                            episode.pages.firstOrNull { it.cid == cid }?.let {
                                updatePlayerInfoV2(
                                    cid = cid,
                                    bvId = episode.bvid,
                                    aid = episode.aid
                                )
                                asVideoPlayerInfo(cid, episode.bvid)
                                return@let
                            }
                        }
                    }
                }
            }

            is ASLinkResultType.BILI.User,
            null -> {

            }
        }
    }

    fun updateSelectedCidList(cid: Long?) {
        cid?.let {
            // 只有当前解析类型是视频时才允许更新CID
            if (_uiState.value.asLinkResultType is ASLinkResultType.BILI.Video) {
                _uiState.value = _uiState.value.copy(
                    downloadInfo = _uiState.value.downloadInfo?.toggleCid(it)
                )
            }
        }
    }

    fun updateSelectedEpIdList(epId: Long?) {
        epId?.let {
            // 只有当前解析类型是动画时才允许更新EpID
            if (_uiState.value.asLinkResultType is ASLinkResultType.BILI.Donghua) {
                _uiState.value = _uiState.value.copy(
                    downloadInfo = _uiState.value.downloadInfo?.toggleEpId(it)
                )
            }
        }
    }


    fun updateSelectDownloadCC(selected: Boolean, ccFileType: CCFileType) {
        if (_uiState.value.asLinkResultType is ASLinkResultType.BILI.Video) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(
                    downloadInfo = _uiState.value.downloadInfo?.copy(
                        ccFileType = ccFileType,
                        downloadCC = selected
                    )
                )
            }
        }
    }

    private fun updatePlayerInfoV2(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
    ) {
        viewModelScope.launch {
            videoInfoRepository.getVideoPlayerInfoV2(cid = cid, bvId = bvId, aid = aid).collect {
                _uiState.emit(
                    _uiState.value.copy(
                        downloadInfo = _uiState.value.downloadInfo?.copy(
                            videoPlayerInfoV2 = it
                        )
                    )
                )
            }
        }
    }


    private suspend fun analysisInputText(inputAsText: String) {
        val asType = AsRegexUtil.parse(inputAsText)
        when (asType) {
            is TextType.BILI.AV -> {
                handleBILIAVAndBV(aid = asType.text.toString())
            }

            is TextType.BILI.BV -> {
                handleBILIAVAndBV(bvId = asType.text)
            }

            is TextType.BILI.EP -> {
                handleDonghuaEp(asType.text)
            }

            is TextType.BILI.ShortLink -> {
                handleShareData(asType.text)
            }

            is TextType.BILI.UserSpace -> {
                handleUserSpace(asType.text)
            }

            is TextType.BILI.SS -> {
                updateSelectSeason(asType.text)
            }

            null -> {
            }

        }
    }

    fun updateSelectSeason(seasonId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            handleDonghuaSeasonId(seasonId)
            FirebaseExt.logBangumiParse(ssId = seasonId)
        }
    }

    private suspend fun handleDonghuaSeasonId(seasonId: Long) {
        videoInfoRepository.getDonghuaSeasonViewInfo(seasonId = seasonId).collect {
            when (it.status) {
                ApiStatus.SUCCESS -> {

                    _uiState.value = _uiState.value.copy(
                        asLinkResultType = ASLinkResultType.BILI.Donghua(
                            (if (it.data?.episodes?.isEmpty() != true) (it.data?.episodes[0]?.epId ?: 0L) else
                                if (it.data?.section?.isEmpty() != true) it.data?.section[0]?.episodes[0]?.epId ?: 0L else -1L),
                            it
                        )
                    )

                    asDonghuaPlayerInfo(it.data, it.data?.episodes?.firstOrNull()?.epId)
                }

                else -> {}
            }
        }
    }

    private suspend fun handleDonghuaEp(epId: Long) {
        // 并发解析
        asDonghuaPlayerInfo(null, epId)
        videoInfoRepository.getDonghuaSeasonViewInfo(epId = epId).collect {
            _uiState.value = _uiState.value.copy(
                asLinkResultType = ASLinkResultType.BILI.Donghua(epId, it)
            )
        }
        FirebaseExt.logBangumiParse(epId = epId)
    }

    private fun asDonghuaPlayerInfo(
        viewInfo: BILIDonghuaSeasonInfo?,
        epId: Long? = null, seasonId: Long? = null
    ) {
        viewModelScope.launch {
            videoInfoRepository.getDonghuaPlayerInfo(epId, seasonId).collect {
                _donghuaPlayerInfo.value = it
                val videoData = it.data

                if (it.status == ApiStatus.SUCCESS) {
                    getDefaultDownloadInfoConfig(
                        videoData?.dash?.video,
                        videoData?.dash?.audio,
                        videoData?.durls,
                        videoData?.supportFormats
                    ) { selectVideoQualityId, selectVideoCode, selectAudioQualityId ->

                        launch(Dispatchers.IO) {
                            val currentUser = userInfoRepository.getCurrentUser()

                            // 如果epId为null或0，则默认选择第一个非会员的epId
                            val defaultEpId = if (epId == null || epId == 0L) {
                                if (currentUser?.isVip() != true) {
                                    viewInfo?.episodes?.firstOrNull { ep -> ep.badge != "会员" }?.epId
                                        ?: 0L
                                } else viewInfo?.episodes?.firstOrNull()?.epId ?: 0L
                            } else {
                                // 如果当前用户不是会员，则选择第一个非会员的epId，否则选择传入的epId
                                if (currentUser?.isVip() != true) {
                                    viewInfo?.episodes?.firstOrNull { ep -> ep.epId == epId && ep.badge != "会员" }?.epId
                                        ?: 0L
                                } else {
                                    epId
                                }
                            }

                            val currentMediaType = _uiState.value.asLinkResultType
                            if (currentMediaType is ASLinkResultType.BILI.Donghua) {
                                _uiState.value = _uiState.value.copy(
                                    downloadInfo = _uiState.value.downloadInfo?.updateForMediaType(
                                        mediaType = currentMediaType,
                                        qualityId = selectVideoQualityId,
                                        code = selectVideoCode,
                                        audioQualityId = selectAudioQualityId,
                                        defaultEpId = defaultEpId
                                    ) ?: DownloadViewInfo(
                                        selectVideoCode = selectVideoCode,
                                        selectVideoQualityId = selectVideoQualityId,
                                        selectAudioQualityId = selectAudioQualityId,
                                        selectedEpId = listOf(defaultEpId).filter { ep -> ep != 0L }
                                    )
                                )
                            }
                        }

                    }
                }

            }
        }
    }

    fun createDownloadTask() {
        _uiState.value = _uiState.value.copy(isCreateDownloadLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value.asLinkResultType != null && uiState.value.downloadInfo != null) {
                var authMid = 0L
                var currentVideo = ""

                when (val result = uiState.value.asLinkResultType) {
                    is ASLinkResultType.BILI.Donghua -> {
                        authMid = result.donghuaViewInfo.data?.upInfo?.mid ?: 0L
                    }

                    is ASLinkResultType.BILI.Video -> {
                        authMid = result.viewInfo.data?.owner?.mid ?: 0L
                        currentVideo = result.viewInfo.data?.bvid ?: ""
                    }

                    else -> {}
                }
// 暂停冻结视频处理
//                val freezeInfo = appAPIService.checkVideoSoFreeze(
//                    currentVideo,
//                    authMid,
//                )

//                if (freezeInfo.getOrNull()?.code != 0) {
//                    _uiState.value = _uiState.value.copy(
//                        isCreateDownloadLoading = false,
//                        appOldSoFreezeBean = freezeInfo.getOrNull()
//                    )
//                    return@launch
//                }

                try {
                    downloadManager.addDownloadTask(
                        uiState.value.asLinkResultType!!,
                        uiState.value.downloadInfo!!
                    )
                    sendToastEvent("已添加到下载队列")
                } catch (e: Exception) {
                    sendToastEvent("添加下载任务失败：${e.message}")
                }
            }
            _uiState.value = _uiState.value.copy(isCreateDownloadLoading = false)
        }
    }

    private fun getDefaultDownloadInfoConfig(
        dashVideoList: List<BILIVideoDash.Video>?,
        dashAudioList: List<BILIVideoDash.Audio>?,
        durlVideoList: List<BILIVideoDurls>?,
        mSupportFormats: List<BILIVideoSupportFormat>?,
        onFinish: (
            selectVideoQualityId: Long?,
            selectVideoCode: String,
            selectAudioQualityId: Long?,
        ) -> Unit
    ) {
        val audioList = dashAudioList
        var selectVideoCode = ""
        val supportFormats = if (dashVideoList != null) {
            // Dash模式
            val mVideoCodingList = mutableSetOf<String>()
            mSupportFormats?.forEach { format ->
                if (format.codecs.isEmpty()){
                    dashVideoList.forEach { video ->
                        val code = video.codecs.split(".")[0]
                        if (code !in mVideoCodingList){
                            mVideoCodingList.add(code)
                        }
                    }
                } else {
                    format.codecs.forEach { code ->
                        mVideoCodingList.add(code.split(".")[0])
                    }
                }
            }
            mSupportFormats?.filter { supportFormat ->
                dashVideoList.any { item -> item.id == supportFormat.quality }
            }?.also {
                // 选择支持清晰度的第一个视频编码
                selectVideoCode = it.firstOrNull()?.codecs?.firstOrNull()?.split(".")[0] ?: ""
            }
        } else {
            // FLV模式
            mSupportFormats?.filter { supportFormat ->
                durlVideoList?.any { item -> item.quality == supportFormat.quality } == true
            }?.ifEmpty {
                // 充电视频下有可能找不到对应的清晰度
                mSupportFormats
            }
        }
        val selectVideoQualityId = supportFormats?.firstOrNull()?.run {
            quality
        }
        val selectAudioQualityId = audioList?.firstOrNull()?.id ?: 0
        onFinish(selectVideoQualityId, selectVideoCode, selectAudioQualityId)
    }

    private fun asVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
    ) {
        viewModelScope.launch {
            videoInfoRepository.getVideoPlayerInfo(cid, bvId, aid).collect {

                _videoPlayerInfo.value = it
                if (it.status == ApiStatus.SUCCESS) {

                    if (it.data?.language == null) {
                        _uiState.emit(
                            _uiState.value.copy(
                                downloadInfo = _uiState.value.downloadInfo?.copy(selectAudioLanguage = null)
                            )
                        )
                    }

                    getDefaultDownloadInfoConfig(
                        it.data?.dash?.video,
                        it.data?.dash?.audio,
                        it.data?.durls,
                        it.data?.supportFormats
                    ) { selectVideoQualityId, selectVideoCode, selectAudioQualityId ->
                        val currentMediaType = _uiState.value.asLinkResultType
                        if (currentMediaType is ASLinkResultType.BILI.Video) {
                            _uiState.value = _uiState.value.copy(
                                downloadInfo = _uiState.value.downloadInfo?.updateForMediaType(
                                    mediaType = currentMediaType,
                                    qualityId = selectVideoQualityId,
                                    code = selectVideoCode,
                                    audioQualityId = selectAudioQualityId,
                                    defaultCid = cid
                                ) ?: DownloadViewInfo(
                                    selectVideoCode = selectVideoCode,
                                    selectVideoQualityId = selectVideoQualityId,
                                    selectAudioQualityId = selectAudioQualityId,
                                    selectedCid = listOf(cid).filter { cid -> cid != 0L },
                                )
                            )
                        }
                        updatePlayerInfoV2(cid = cid, bvId = bvId, aid = aid)
                    }
                }
            }
        }
    }


    private suspend fun handleUserSpace(text: String) {
        userInfoRepository.getUserPageInfo(text.toLong()).collect {
            _uiState.value =
                _uiState.value.copy(
                    asLinkResultType = ASLinkResultType.BILI.User(it)
                )
        }
    }

    /**
     * 加载APP端分享视频
     */
    private suspend fun handleShareData(url: String) {
        runCatching { videoInfoRepository.shortLink(url) }
            .onSuccess { analysisInputText(it) }
            .onFailure {}
    }


    private suspend fun handleBILIAVAndBV(aid: String? = null, bvId: String? = null) {
        videoInfoRepository.getVideoView(aid = aid, bvId = bvId).collect {
            if (it.data?.redirectUrl != null) {
                analysisInputText(it.data?.redirectUrl ?: "")
                return@collect
            }
            when (it.status) {
                ApiStatus.SUCCESS -> {
                    FirebaseExt.logVideoParse(it.data?.bvid)
                    // 检查充电
                    if (it.data?.isUpowerExclusive == true) {
                        val boostInfo = loadBoostVideoInfo()
                        _boostVideoInfo.emit(boostInfo.getOrNull())
                    }
                    asVideoPlayerInfo(it.data?.cid ?: 0, it.data?.bvid, it.data?.aid)
                    // 检查互动视频
                    if (it.data?.rights?.isSteinGate != 0L) {
                        handleInteractiveVideo(it.data?.cid ?: 0, it.data?.bvid, it.data?.aid)
                    }
                }

                else -> {}
            }
            _uiState.value = _uiState.value.copy(
                asLinkResultType = ASLinkResultType.BILI.Video(it.data?.bvid ?: "", it)
            )
        }
    }

    private suspend fun AnalysisViewModel.handleInteractiveVideo(
        cid: Long,
        bvId: String?,
        aid: Long?
    ) {
        // 获取播放信息V2，里面包含graphVersion
        val playV2Info =
            videoInfoRepository.getVideoPlayerInfoV2(cid, bvId = bvId, aid = aid).last()
        videoInfoRepository.getSteinEdgeInfoV2(
            aid = aid?.toString(),
            bvId = bvId,
            graphVersion = playV2Info.data?.interaction?.graphVersion ?: 0L
        )
            .collect { result ->
                _interactiveVideo.value = result
            }
    }


    /**
     * 便捷的扩展属性，简化UI中的类型判断
     */
    val AnalysisUIState.isVideoType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.Video

    val AnalysisUIState.isDonghuaType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.Donghua

    val AnalysisUIState.isUserType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.User

    val AnalysisUIState.canDownload: Boolean
        get() = isVideoType || isDonghuaType

    /**
     * 获取当前选中的项目数量
     */
    val AnalysisUIState.selectedItemCount: Int
        get() = when {
            isVideoType -> downloadInfo?.selectedCid?.size ?: 0
            isDonghuaType -> downloadInfo?.selectedEpId?.size ?: 0
            else -> 0
        }

    /**
     * 获取当前内容的类型描述
     */
    val AnalysisUIState.contentTypeDescription: String
        get() = when {
            isVideoType -> "视频"
            isDonghuaType -> "动画"
            isUserType -> "用户"
            else -> "未知"
        }

    fun updateSelectSingleModel(isSelectSingleModel: Boolean) {
        viewModelScope.launch {
            val currentState = _uiState.replayCache.firstOrNull() ?: AnalysisUIState()
            _uiState.emit(
                currentState.copy(
                    isSelectSingleModel = isSelectSingleModel,
                )
            )
        }
    }

    /**
     * 清空所有选中项
     */
    fun clearAllSelections() {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(
                selectedCid = listOf(),
                selectedEpId = listOf()
            )
        )
    }


    fun updateVideoCode(code: String) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(selectVideoCode = code)
        )
    }

    fun updateVideoQualityId(qualityId: Long?) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(selectVideoQualityId = qualityId)
        )
    }

    fun updateAudioQualityId(qualityId: Long?) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(selectAudioQualityId = qualityId)
        )
    }

    fun updateDownloadMode(downloadMode: DownloadMode) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(downloadMode = downloadMode)
        )
    }

    fun updateEpisodeListMode(it: AppSettings.EpisodeListMode) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                episodeListMode = it
            )
            appSettingsRepository.updateEpisodeListMode(it)
        }
    }

    fun updateDownloadDanmaku(it: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(downloadDanmaku = it)
        )
    }

    fun updateDownloadMedia(state: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(downloadMedia = state)
        )
    }

    fun updateEmbedCover(state: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(embedCover = state)
        )
    }
    fun updateEmbedCC(state: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(embedCC = state)
        )
    }

    fun updateEmbedDanmaku(state: Boolean) {
        _uiState.value = _uiState.value.copy(
            downloadInfo = _uiState.value.downloadInfo?.copy(embedDanmaku = state)
        )
    }

    fun updateAudioLanguage(language: BILIVideoLanguageItem) {
        if (language == _uiState.value.downloadInfo?.selectAudioLanguage) {
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(selectAudioLanguage = null)
            )
        } else {
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(selectAudioLanguage = language)
            )
        }
    }

    fun closeShowVideoFreezeTip() {
        _uiState.value = _uiState.value.copy(
            appOldSoFreezeBean = null
        )
    }

    fun onUpdateSelectCidList(cidList: List<Long>) {
        // 判断cidList是不是在_uiState.value.downloadInfo.selectedCid里都有
        if (_uiState.value.downloadInfo?.selectedCid?.containsAll(cidList) == true) {
            // 排除后更新
            val currentCidList =
                (_uiState.value.downloadInfo?.selectedCid ?: listOf()).toMutableList()
                    .apply { removeAll(cidList) }
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(
                    selectedCid = currentCidList
                )
            )
        } else {
            // 去重加入
            val currentCidList =
                (TreeSet(_uiState.value.downloadInfo?.selectedCid ?: listOf()) + cidList).toList()
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(
                    selectedCid = currentCidList,
                )
            )
        }

    }
    fun onUpdateSelectEpIdList(epIdList: List<Long>) {
        if (_uiState.value.downloadInfo?.selectedEpId?.containsAll(epIdList) == true) {
            // 排除后更新
            val currentEpIdList =
                (_uiState.value.downloadInfo?.selectedEpId ?: listOf()).toMutableList()
                    .apply { removeAll(epIdList) }
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(
                    selectedEpId = currentEpIdList
                )
            )
        } else {
            // 去重加入
            val currentEpIdList =
                (TreeSet(_uiState.value.downloadInfo?.selectedEpId ?: listOf()) + epIdList).toList()
            _uiState.value = _uiState.value.copy(
                downloadInfo = _uiState.value.downloadInfo?.copy(
                    selectedEpId = currentEpIdList,
                )
            )
        }

    }

}