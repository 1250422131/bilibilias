package com.imcys.bilibilias.ui.tool

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val json: Json,
) : BaseViewModel() {

    private val _toolState = MutableStateFlow(ToolState())
    val toolState = _toolState.asStateFlow()

    init {
        videoRepository.videoDetails2
            .onEach { res ->
                when (res) {
                    is Result.Error -> TODO()
                    Result.Loading -> {}
                    is Result.Success -> _toolState.update { it.copy(videoDetails = res.data, isShowVideoCard = true) }
                }
            }.launchIn(viewModelScope)

        videoRepository.bangumi.onEach { res ->
            when (res) {
                is Result.Error -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> {
                    val bangumi = json.decodeFromString<Bangumi>(res.data)
                    _toolState.update {
                        it.copy(bangumi = bangumi.result, isShowVideoCard = true)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun parseTextTypes(text: String): VideoType {
        return if (AsVideoUtils.isAV(text)) {
            val id = AsVideoUtils.getAvid(text)
            if (id != null) VideoType.AV(id) else VideoType.None
        } else if (AsVideoUtils.isBV(text)) {
            val id = AsVideoUtils.getBvid(text)
            if (id != null) VideoType.BV(id) else VideoType.None
        } else if (AsVideoUtils.isEp(text)) {
            val id = AsVideoUtils.getEpid(text)
            if (id != null) VideoType.EP(id) else VideoType.None
        } else if (AsVideoUtils.isShortLink(text)) {
            val id = AsVideoUtils.getShortLink(text)
            if (id != null) VideoType.ShortLink(id) else VideoType.None
        } else {
            VideoType.None
        }
    }

    fun parsesBvOrAvOrEp(text: String) {
        if (text.trim().isBlank()) return
        val type = parseTextTypes(text)
        Timber.tag(TAG).i("解析结果=$type")
        _toolState.update { it.copy(text = text, videoType = type) }
        when (type) {
            is VideoType.AV -> handelAV(type.id)
            is VideoType.BV -> handelBV(type.id)
            is VideoType.EP -> handelEP(type.id)
            is VideoType.ShortLink -> handelHttp(type.url)
            VideoType.None -> _toolState.update {
                it.copy(isShowVideoCard = false, inputError = true)
            }
        }
    }

    private fun handelEP(id: String) {
        launchIO {
            videoRepository.getEp(id)
        }
    }

    private fun handelHttp(url: String) {
        launchIO {
            when (val shortLink = videoRepository.shortLink(url)) {
                is Result.Error -> TODO()
                Result.Loading -> {}
                is Result.Success -> parseTextTypes(shortLink.data.request.url.toString())
            }
        }
    }

    private fun handelAV(id: String) {
        launchIO {
            videoRepository.getVideoDetailsByAid(id)
        }
    }

    private fun handelBV(id: String) {
        launchOnIO {
            videoRepository.getVideoDetailsByBvid(id)
        }
    }

    fun clearText() {
        _toolState.update {
            it.copy(text = "", inputError = false, isShowVideoCard = false)
        }
    }
}

sealed class VideoType {
    data object None : VideoType()
    data class BV(val id: String) : VideoType()
    data class AV(val id: String) : VideoType()
    data class EP(val id: String) : VideoType()
    data class ShortLink(val url: String) : VideoType()
}

data class ToolState(
    val text: String = "",
    val inputError: Boolean = false,
    val isShowVideoCard: Boolean = false,
    val videoDetails: VideoDetails = VideoDetails(),
    val bangumi: Bangumi.Result = Bangumi.Result(),
    val videoType: VideoType = VideoType.None
)

private const val TAG = "ToolViewModel"
