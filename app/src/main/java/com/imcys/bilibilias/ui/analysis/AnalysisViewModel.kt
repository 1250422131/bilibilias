package com.imcys.bilibilias.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.AsRegexUtil
import com.imcys.bilibilias.common.utils.TextType
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
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
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    data class UIState(
        val inputAsText: String = "",
        val linkType: TextType? = null,
        val asLinkResultType: ASLinkResultType? = null,
        val isBILILogin: Boolean = false,
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
                    asDonghuaPlayerInfo(it.data?.episodes?.firstOrNull()?.epId)
                }

                else -> {}
            }
            _uiState.value = _uiState.value.copy(
                asLinkResultType = ASLinkResultType.BILI.Donghua(-1, it)
            )
        }
    }

    private suspend fun handleDonghuaEp(epId: Long) {
        // 并发解析
        asDonghuaPlayerInfo(epId)
        videoInfoRepository.getDonghuaSeasonViewInfo(epId = epId).collect {
            _uiState.value = _uiState.value.copy(
                asLinkResultType = ASLinkResultType.BILI.Donghua(epId, it)
            )
        }
    }

    private fun asDonghuaPlayerInfo(epId: Long? = null, seasonId: Long? = null) {
        viewModelScope.launch {
            videoInfoRepository.getDonghuaPlayerInfo(epId, seasonId).collect {
                _donghuaPlayerInfo.value = it
            }
        }
    }

    private fun asVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
    ) {
        viewModelScope.launch {
            videoInfoRepository.getVideoPlayerInfo(cid, bvId, aid).collect {
                _videoPlayerInfo.value = it
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

}