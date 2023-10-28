package com.imcys.bilibilias.ui.tool

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.digitalConversion
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.common.base.utils.AsVideoUtils
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.request
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
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
    var inputText by mutableStateOf("")
        private set

    init {
        videoRepository.videoDetails2
            .onEach { res ->
                when (res) {
                    is Result.Error -> TODO()
                    Result.Loading -> {}
                    is Result.Success -> _toolState.update {
                        val data = res.data
                        it.copy(
                            isShowVideoCard = true,
                            pic = data.pic,
                            title = data.title,
                            desc = data.descV2?.first()?.rawText ?: data.desc,
                            view = data.stat.view.digitalConversion(),
                            danmaku = data.stat.danmaku.digitalConversion(),
                        )
                    }
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

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val result = snapshotFlow { inputText }
        .filterNot { it.isBlank() }
        .debounce(300)
        .distinctUntilChanged()
        .mapLatest { parsingTextTypes(it) }
        .onEach { type ->
            Timber.tag(TAG).i("解析结果=$type")
            when (type) {
                is VideoType.AV -> handelAV(type.id)
                is VideoType.BV -> handelBV(type.id)
                is VideoType.EP -> handelEP(type.id)
                is VideoType.ShortLink -> handelHttp(type.url)
                VideoType.None -> _toolState.update {
                    it.copy(isShowVideoCard = false, inputError = true)
                }
            }
        }.launchIn(viewModelScope)

    fun updateInput(newText: String) {
        inputText = newText
    }

    private fun parsingTextTypes(text: String): VideoType {
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
                is Result.Success -> parsingTextTypes(shortLink.data.request.url.toString())
            }
        }
    }

    private suspend fun handelAV(id: String): Result<VideoDetails> =
        videoRepository.getVideoDetailsByAid(id)

    private suspend fun handelBV(id: String): Result<VideoDetails> =
        videoRepository.getVideoDetailsByBvid(id)

    fun clearInput() {
        inputText = ""
        _toolState.update {
            it.copy(inputError = false, isShowVideoCard = false)
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
    val inputError: Boolean = false,
    val isShowVideoCard: Boolean = false,
    val bangumi: Bangumi.Result = Bangumi.Result(),
    val videoType: VideoType = VideoType.None,
    val pic: String = "",
    val title: String = "",
    val desc: String = "",
    val view: String = "",
    val danmaku: String = "",
)

private const val TAG = "ToolViewModel"
