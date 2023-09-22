package com.imcys.bilibilias.ui.tool

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.model.VideoBaseBean
import com.imcys.bilibilias.common.base.repository.AsRepository
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils.httpClient
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val asRepository: AsRepository
) : BaseViewModel() {
    private val _toolState = MutableStateFlow(ToolState())
    val toolState = _toolState.asStateFlow()

    fun getOldItemList() {}

    /**
     * todo 短链接处理有问题, ep 未处理
     */
    fun parsesBvOrAvOrEp(text: String) {
        if (text.trim().isBlank()) return
        _toolState.update {
            it.copy(query = text)
        }
        Timber.tag(TAG).i("解析的链接=$text")
        if (AsVideoUtils.isBVStart(text)) {
            handelBV(text)
            return
        }
        if (AsVideoUtils.isAV(text)) {
            handelAV(text)
            return
        }
        // 短链接
        if (AsVideoUtils.isBVHttp(text)) {
            handelHttp(text)
        }
    }

    private fun handelHttp(text: String) {
        val url = AsVideoUtils.getBvHttp(text)!!
        launchIO {
            httpClient.prepareGet(url).execute { response ->
                val bvid = AsVideoUtils.getBvid(response.request.url.toString())!!
                parsesBvOrAvOrEp(bvid)
            }
        }
    }

    private fun handelAV(text: String) {
        val aid = AsVideoUtils.getAvid(text)
        if (aid == null) {
            setIsInputError(true)
            _toolState.update {
                it.copy(isShowVideoCard = false)
            }
            return
        }
        launchIO {
            setIsInputError(!videoRepository.getVideoDetailsAvid(aid, ::setVideoMate))
        }
        _toolState.update {
            it.copy(query = "AV$aid", isShowVideoCard = true)
        }
    }

    private fun handelBV(text: String) {
        val bvid = AsVideoUtils.getBvid(text)
        if (bvid == null) {
            setIsInputError(true)
            _toolState.update {
                it.copy(isShowVideoCard = false)
            }
            return
        }
        launchIO {
            setIsInputError(!videoRepository.getVideoDetailsByBvid(bvid, ::setVideoMate))
        }
        _toolState.update {
            it.copy(query = bvid, isShowVideoCard = true)
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
                    videoDetails.bvid,
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
            it.copy(query = "", isInputError = false, isShowVideoCard = false)
        }
    }
}

private const val TAG = "ToolViewModel"

data class ToolState(
    val query: String = "",
    val isInputError: Boolean = false,
    val isShowVideoCard: Boolean = false,
    val videoMate: VideoMate = VideoMate(),
    val tools: ImmutableList<ToolMate> = persistentListOf()
)

data class VideoMate(
    val videoId: String = "",
    val pic: String = "",
    val title: String = "",
    val desc: String = "",
    val playVolume: String = "",
    val danmaku: String = "",
)

data class ToolMate(
    val color: Int = 0,
    val imgUrl: String = "",
    val title: String = "",
    val toolCode: Int = 0,
)
