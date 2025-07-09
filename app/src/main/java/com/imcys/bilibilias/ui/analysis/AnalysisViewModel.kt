package com.imcys.bilibilias.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.AsRegexUtil
import com.imcys.bilibilias.common.utils.TextType
import com.imcys.bilibilias.data.repository.VideoInfoRepository
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.data.model.video.ASLinkResultType
import com.imcys.bilibilias.data.repository.UserInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AnalysisViewModel(
    usersDataSource: UsersDataSource,
    private val videoInfoRepository: VideoInfoRepository,
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    data class UIState(
        val inputAsText: String = "",
        val linkType: TextType? = null,
        val asLinkResultType: ASLinkResultType? = null,
        val isBILILogin: Boolean = false
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    isBILILogin = usersDataSource.isLogin()
                )
            }
        }
    }

    /**
     * 当前仅支持解析B站视频，后续兼容AcFun
     */
    fun updateInputAsText(inputAsText: String) {
        if (inputAsText.isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    inputAsText = inputAsText
                )
            }
            analysisInputText(inputAsText)
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

            is TextType.BILI.EP -> TODO()

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

    private suspend fun handleUserSpace(text: String) {
        userInfoRepository.getUserPageInfo(text.toLong()).collect {
            _uiState.update { state ->
                state.copy(
                    asLinkResultType = ASLinkResultType.BILI.User(it)
                )
            }
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
            _uiState.update { state ->
                state.copy(
                    asLinkResultType = ASLinkResultType.BILI.Video(it)
                )
            }
        }
    }

}