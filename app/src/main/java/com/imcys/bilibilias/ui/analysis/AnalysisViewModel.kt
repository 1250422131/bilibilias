package com.imcys.bilibilias.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.AsRegexUtil
import com.imcys.bilibilias.common.utils.TextType
import com.imcys.bilibilias.data.model.download.DownloadViewInfo
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.database.entity.download.DownloadMode
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.dwonload.DownloadManager
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoDash
import com.imcys.bilibilias.network.model.video.BILIVideoDurls
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoSupportFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@OptIn(FlowPreview::class)
class AnalysisViewModel(
    usersDataSource: UsersDataSource,
    private val videoInfoRepository: VideoInfoRepository,
    private val userInfoRepository: UserInfoRepository,
    private val downloadManager: DownloadManager
) : ViewModel() {


    data class UIState(
        val inputAsText: String = "",
        val linkType: TextType? = null,
        val asLinkResultType: ASLinkResultType? = null,
        val isBILILogin: Boolean = false,
        val downloadInfo: DownloadViewInfo? = null,
        val isCreateDownloadLoading: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    private val _donghuaPlayerInfo =
        MutableStateFlow<NetWorkResult<BILIDonghuaPlayerInfo?>>(emptyNetWorkResult())
    val donghuaPlayerInfo = _donghuaPlayerInfo.asStateFlow()

    private val _videoPlayerInfo =
        MutableStateFlow<NetWorkResult<BILIVideoPlayerInfo?>>(emptyNetWorkResult())
    val videoPlayerInfo = _videoPlayerInfo.asStateFlow()


    private val debounceTime = 1000L // 防抖时间
    private val debounceJob: MutableStateFlow<String?> = MutableStateFlow(null)


    init {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                isBILILogin = usersDataSource.isLogin()
            )
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

    }

    /**
     * 当前仅支持解析B站视频，后续兼容AcFun
     */
    fun updateInputAsText(inputAsText: String) {
        _uiState.value = _uiState.value.copy(
            inputAsText = inputAsText
        )
        if (inputAsText.isEmpty()) return
        debounceJob.value = inputAsText
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

            null -> {
            }
        }
    }

    fun updateSelectSeason(seasonId: Long) {
        viewModelScope.launch {
            handleDonghuaSeasonId(seasonId)
        }
    }

    private suspend fun handleDonghuaSeasonId(seasonId: Long) {
        videoInfoRepository.getDonghuaSeasonViewInfo(seasonId = seasonId).collect {
            when (it.status) {
                ApiStatus.SUCCESS -> {

                    _uiState.value = _uiState.value.copy(
                        asLinkResultType = ASLinkResultType.BILI.Donghua(
                            (if (it.data?.episodes?.isEmpty() != true) it.data?.episodes[0]?.epId else
                                if (it.data?.section?.isEmpty() != true) it.data?.section[0] else -1) as Long,
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
    }

    private fun asDonghuaPlayerInfo(
        viewInfo: BILIDonghuaSeasonInfo?,
        epId: Long? = null, seasonId: Long? = null
    ) {
        viewModelScope.launch {
            videoInfoRepository.getDonghuaPlayerInfo(epId, seasonId).collect {
                _donghuaPlayerInfo.value = it

                if (it.status == ApiStatus.SUCCESS) {
                    getDefaultDownloadInfoConfig(
                        it.data?.dash?.video,
                        it.data?.dash?.audio,
                        it.data?.durls,
                        it.data?.supportFormats
                    ) { selectVideoQualityId, selectVideoCode, selectAudioQualityId ->
                        val defaultEpId = if (epId == null || epId == 0L) {
                            viewInfo?.episodes?.firstOrNull()?.epId ?: 0L
                        } else epId

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
                                    selectedEpId = listOf(defaultEpId)
                                )
                            )
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
                downloadManager.addDownloadTask(
                    uiState.value.asLinkResultType!!,
                    uiState.value.downloadInfo!!
                )
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
                format.codecs.forEach { code ->
                    mVideoCodingList.add(code.split(".")[0])
                }
            }
            selectVideoCode = mVideoCodingList.firstOrNull() ?: ""
            mSupportFormats?.filter { supportFormat ->
                dashVideoList.any { item -> item.id == supportFormat.quality }
            }
        } else {
            // FLV模式
            mSupportFormats?.filter { supportFormat ->
                durlVideoList?.any { item -> item.quality == supportFormat.quality } == true
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
                                    selectedCid = listOf(cid)
                                )
                            )
                        }
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
                    asVideoPlayerInfo(it.data?.cid ?: 0, it.data?.bvid, it.data?.aid)
                }

                else -> {}
            }
            _uiState.value = _uiState.value.copy(
                asLinkResultType = ASLinkResultType.BILI.Video(it.data?.bvid ?: "", it)
            )
        }
    }

    /**
     * 便捷的扩展属性，简化UI中的类型判断
     */
    val UIState.isVideoType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.Video

    val UIState.isDonghuaType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.Donghua

    val UIState.isUserType: Boolean
        get() = asLinkResultType is ASLinkResultType.BILI.User

    val UIState.canDownload: Boolean
        get() = isVideoType || isDonghuaType

    /**
     * 获取当前选中的项目数量
     */
    val UIState.selectedItemCount: Int
        get() = when {
            isVideoType -> downloadInfo?.selectedCid?.size ?: 0
            isDonghuaType -> downloadInfo?.selectedEpId?.size ?: 0
            else -> 0
        }

    /**
     * 获取当前内容的类型描述
     */
    val UIState.contentTypeDescription: String
        get() = when {
            isVideoType -> "视频"
            isDonghuaType -> "动画"
            isUserType -> "用户"
            else -> "未知"
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
}