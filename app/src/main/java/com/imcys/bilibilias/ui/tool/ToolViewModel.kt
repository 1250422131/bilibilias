package com.imcys.bilibilias.ui.tool

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.model.VideoBaseBean
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.AsVideoNumUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils.httpClient
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(private val videoRepository: VideoRepository) : BaseViewModel() {
    private val _toolState = MutableStateFlow(ToolState())
    val toolState = _toolState.asStateFlow()

    fun parsesBvOrAvOrEp(text: String) {
        if (text.isBlank()) return
        _toolState.update {
            it.copy(query = text)
        }
        Timber.tag(TAG).i("解析的链接=$text")
        if (AsVideoNumUtils.isBV(text)) {
            handelBV(text)
            return
        }
        if (AsVideoNumUtils.isAV(text)) {
            handelAV(text)
            return
        }
    }

    private fun handelAV(text: String) {
        val aid = AsVideoNumUtils.getAvid(text)
        if (aid == null) {
            setIsInputError(true)
            return
        }
        launchIO {
            setIsInputError(!videoRepository.getVideoDetailsAvid(aid, ::setVideoMate))
        }
        _toolState.update {
            it.copy(query = "AV$aid")
        }
    }

    private fun handelBV(text: String) {
        val bvid = AsVideoNumUtils.getBvid(text)
        if (bvid == null) {
            setIsInputError(true)
            return
        }
        launchIO {
            setIsInputError(!videoRepository.getVideoDetailsBvid(bvid, ::setVideoMate))
        }
        _toolState.update {
            it.copy(query = "BV$bvid")
        }
    }

    private fun setIsInputError(isError: Boolean) {
        _toolState.update {
            it.copy(isInputError = isError)
        }
    }

    private fun setVideoMate(videoDetails: VideoBaseBean) {
        _toolState.update {
            it.copy(
                videoMate = VideoMate(
                    videoDetails.pic,
                    videoDetails.title,
                    videoDetails.desc,
                    videoDetails.stat.view.toString(),
                    videoDetails.stat.danmaku.toString()
                )
            )
        }
    }

    /**
     * 加载APP端分享视频
     * @param toString String
     */
    private fun loadShareData(url: String) = flow {
        val text = httpClient.get(url).bodyAsText()
        Timber.tag("tool").d(url)
        emit(text)
    }

    private fun getEpVideo(epId: String) {
        TODO("后端数据响应没有字段 data")
        launchIO {
            val body = httpClient.get(BilibiliApi.bangumiVideoDataPath) {
                parameter("ep_id", epId)
            }.body<BangumiSeasonBean>()
        }
    }

    fun clearText() {
        _toolState.update {
            it.copy(query = "", isInputError = false)
        }
    }
}

private const val TAG = "ToolViewModel"

data class ToolState(
    val query: String = "",
    val isInputError: Boolean = false,
    val videoMate: VideoMate = VideoMate()
)

data class VideoMate(
    val pic: String = "",
    val title: String = "",
    val desc: String = "",
    val playVolume: String = "",
    val danmaku: String = "",
)
