package com.imcys.bilibilias.ui.tool

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.model.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils.httpClient
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : BaseViewModel() {

    private val _toolState = MutableStateFlow(ToolState())
    val toolState = _toolState.asStateFlow()
    init {
        videoRepository.testSharedFlow.onEach { v ->
            _toolState.update {
                it.copy(videoDetails = v, isShowVideoCard = true)
            }
        }.launchIn(viewModelScope)
    }
    private sealed class VideoType {
        data object None : VideoType()
        data class BV(val id: String) : VideoType()
        data class AV(val id: String) : VideoType()
        data class EP(val id: String) : VideoType()
        data class ShortLink(val url: String) : VideoType()
    }

    private fun parseTextTypes(text: String): VideoType {
        return if (AsVideoUtils.isAV(text)) {
            val id = AsVideoUtils.getAvid(text)
            if (id != null) VideoType.AV(id) else VideoType.None
        } else if (AsVideoUtils.isBV(text)) {
            val id = AsVideoUtils.getBvid(text)
            if (id != null) VideoType.BV(id) else VideoType.None
        } else if (AsVideoUtils.isShortLink(text)) {
            val id = AsVideoUtils.getShortLink(text)
            if (id != null) VideoType.ShortLink(id) else VideoType.None
        } else if (AsVideoUtils.isEp(text)) {
            val id = AsVideoUtils.getEpid(text)
            if (id != null) VideoType.EP(id) else VideoType.None
        } else {
            VideoType.None
        }
    }

    fun parsesBvOrAvOrEp(text: String) {
        if (text.trim().isBlank()) return
        _toolState.update { it.copy(text = text) }
        Timber.tag(TAG).i("解析的链接=$text")
        when (val video = parseTextTypes(text)) {
            is VideoType.AV -> handelAV(video.id)
            is VideoType.BV -> handelBV(video.id)
            is VideoType.EP -> handelEP(video.id)
            is VideoType.ShortLink -> handelHttp(video.url)
            VideoType.None -> {
                _toolState.update {
                    it.copy(isShowVideoCard = false, inputError = true)
                }
            }
        }
    }

    private fun handelEP(id: String) {
        launchIO {
            val bangumi = videoRepository.get剧集基本信息(id)
            _toolState.update {
                it.copy(
                    // videoMate = VideoMate(
                    //     bangumi.episodes.first().bvid,
                    //     bangumi.cover,
                    //     bangumi.evaluate,
                    //     bangumi.stat.views.toString(),
                    //     bangumi.stat.danmakus.toString(),
                    // )
                )
            }
        }
        _toolState.update {
            it.copy(isShowVideoCard = true)
        }
    }

    private fun handelHttp(url: String) {
        launchIO {
            httpClient.prepareGet(url).execute { response ->
                val bvid = AsVideoUtils.getBvid(response.request.url.toString())!!
                parsesBvOrAvOrEp(bvid)
            }
        }
    }

    private fun handelAV(id: String) {
        launchIO {
            videoRepository.getVideoDetailsAvid(id)
        }
    }

    private fun handelBV(id: String) {
        launchIO {
            videoRepository.getVideoDetailsByBvid(id)
        }
    }

    private fun setVideoMate(videoDetails: VideoDetails) {
        _toolState.update {
            it.copy(
                // videoMate = VideoMate(
                //     videoDetails.bvid,
                //     videoDetails.pic,
                //     videoDetails.title,
                //     videoDetails.desc,
                //     videoDetails.stat.view.toString(),
                //     videoDetails.stat.danmaku.toString()
                // )
            )
        }
    }

    fun clearSearchText() {
        _toolState.update {
            it.copy(text = "", inputError = false, isShowVideoCard = false)
        }
    }
}

private const val TAG = "ToolViewModel"

data class ToolState(
    val text: String = "",
    val inputError: Boolean = false,
    val isShowVideoCard: Boolean = false,
    val videoDetails: VideoDetails = VideoDetails()
)
