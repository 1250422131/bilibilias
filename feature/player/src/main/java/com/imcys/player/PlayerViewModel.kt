package com.imcys.player

import androidx.annotation.OptIn
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.model.PlayerInfo
import com.imcys.model.video.PageData
import com.imcys.network.download.DownloadListHolders
import com.imcys.network.download.DownloadManage
import com.imcys.network.repository.VideoRepository
import com.imcys.network.repository.space.SpaceRepository
import com.imcys.player.navigation.A_ID
import com.imcys.player.navigation.BV_ID
import com.imcys.player.navigation.C_ID
import com.imcys.player.state.PlayInfoUiState
import com.imcys.player.state.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val spaceRepository: SpaceRepository,
    val downloadListHolders: DownloadListHolders,
    private val savedStateHandle: SavedStateHandle,
    private val downloadManage: DownloadManage
) : ViewModel() {

    private val aid = savedStateHandle.getStateFlow(A_ID, "")
    private val bvid = savedStateHandle.getStateFlow(BV_ID, "")
    private val cid = savedStateHandle.getStateFlow(C_ID, "")

    private val info = combine(aid, bvid, cid, ::Triple)
        .shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val playerInfoUiState = info.map { (a, b, c) ->
        videoRepository.detail(b)
    }.asResult()
        .map { result ->
            when (result) {
                is Result.Error -> PlayInfoUiState.LoadFailed
                Result.Loading -> PlayInfoUiState.Loading
                is Result.Success -> {
                    PlayInfoUiState.Success(
                        title = result.data.title,
                        pic = result.data.pic,
                        desc = result.data.descV2?.firstOrNull()?.rawText ?: result.data.desc,
                        pageData = result.data.pageData,
                        aid = result.data.aid,
                        bvid = result.data.bvid,
                        cid = result.data.cid,
                        stat = result.data.stat
                    )
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, PlayInfoUiState.Loading)

    /**
     *  播放器
     */
    val playerUiState = info.map { (a, b, c) ->
        videoRepository.getPlayerPlayUrl(b, c.toLong(),)
    }.asResult()
        .map { result ->
            when (result) {
                is Result.Error -> PlayerUiState.LoadFailed
                Result.Loading -> PlayerUiState.Loading
                is Result.Success -> result.data.mapToPlayerUiState()
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, PlayerUiState.Loading)

    private fun PlayerInfo.mapToPlayerUiState(): PlayerUiState.Success {
        val pairs = acceptDescription.zip(acceptQuality).toImmutableList()
        return PlayerUiState.Success(
            qualityDescription = pairs,
            video = dash.video.toImmutableList(),
            audio = dash.audio.toImmutableList(),
            dolby = dash.dolby,
            duration = dash.duration,
            pageData = emptyList()
        )
    }

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    /**
     * key 是视频清晰度
     * 126,120,112,80,64,32,16
     */
    private val qualityGroup = sortedMapOf<Int, List<com.imcys.model.Dash.Video>>()

    /**
     * 下载视频
     */
    fun addToDownloadQueue(pageData: List<PageData>, quality: Int) {
        // todo 或许可以只写 弹幕文件 和 entry.json
        // 弹幕文件或许从pb接口尝试
        viewModelScope.launch {
        }
    }

    // 选择视频清晰度
    // todo 还需要查看手机支持的编码器，以选择合适的 url？
    @OptIn(UnstableApi::class)
    fun selectedQuality(quality: Int) {
        val videoList = qualityGroup[quality]?.sortedBy { it.codecid }
        if (videoList == null) {
            Timber.d("清晰度为空$qualityGroup")
            _playerState.update { state ->
                val v = qualityGroup.maxBy { it.key }.value.minBy { it.codecid }
                state.copy(video = v, currentQn = v.id)
            }
        } else {
            for (v in videoList) {
                if (selectDecoder(v.codecs) != null) {
                    _playerState.update { state -> state.copy(video = v, currentQn = quality) }
                    break
                }
            }
        }
    }

    // 选择解码器
    @OptIn(UnstableApi::class)
    fun selectDecoder(codec: String): MediaCodecInfo? {
        val mimeType = getMimeType(codec)
        if (mimeType != null) {
            val decoderInfo = getDecoderInfo(mimeType)
            if (decoderInfo != null) {
                Timber.d("解码器信息=$decoderInfo,硬解=${!decoderInfo.name.startsWith("OMX.google")}")
                return decoderInfo
            }
        }
        return null
    }

    // 选择视频子集
    fun selectedPage(cid: String, aid: Long) {
        val bvid = _playerState.value.bvid
        viewModelScope.launch {
            val res = videoRepository.getPlayerPlayUrl(bvid, cid.toLong())
            setQualityGroup(res.dash.video)
            val qn = _playerState.value.currentQn
            selectedQuality(qn)
            _playerState.update { state ->
                state.copy(cid = cid, audio = res.dash.audio.maxBy { it.id })
            }
        }
    }

    fun admDownload(url: String) {
        downloadManage.admDownload(url)
    }

    fun idmDownload(url: String) {
        downloadManage.idmDownload(url)
    }

    private fun setQualityGroup(videos: List<com.imcys.model.Dash.Video>) {
        qualityGroup.clear()
        qualityGroup.putAll(videos.画质分组())
    }
}

@Stable
data class PlayerState(
    val title: String = "",
    val desc: String = "",
    val bvid: String = "",
    val aid: Long = 0,
    val cid: String = "",
    val pic: String = "",
    val like: Int = 0,
    val coins: Int = 0,
    val favorite: Int = 0,
    @Deprecated("")
    val videoDetails: com.imcys.model.VideoDetails = com.imcys.model.VideoDetails(),

    val hasLike: Boolean = false,
    val hasCoins: Boolean = false,
    val hasCollection: Boolean = false,
    val share: Int = 0,

    val descriptionAndQuality: List<Pair<String, Int>> = emptyList(),
    val pageData: List<PageData> = emptyList(),

    val currentQn: Int = 0,

    val video: com.imcys.model.Dash.Video = com.imcys.model.Dash.Video(),
    val audio: com.imcys.model.Dash.Audio = com.imcys.model.Dash.Audio(),
)

fun List<com.imcys.model.Dash.Video>.画质分组(): Map<Int, List<com.imcys.model.Dash.Video>> =
    groupBy { it.id }

fun List<com.imcys.model.Dash.Video>.画质最高队列(): List<com.imcys.model.Dash.Video> =
    groupBy { it.id }
        .maxBy { it.key }
        .value
        .sortedBy { it.codecid }

@OptIn(UnstableApi::class)
fun getDecoderInfo(mimeType: String) = MediaCodecUtil.getDecoderInfo(mimeType, false, false)

@OptIn(UnstableApi::class)
fun getMimeType(codec: String) = MimeTypes.getMediaMimeType(codec)

private const val DOWNLOAD_QUALITY = "download_quality"
private const val DOWNLOAD_PAGES = "download_pages"

const val QUALITY_8K = 127
const val QUALITY_DOLBY = 126
const val QUALITY_HDR = 125
const val QUALITY_4K = 120
const val QUALITY_1080P_60 = 116
const val QUALITY_1080P_PLUS = 112
const val QUALITY_1080P = 80
const val QUALITY_720P_60 = 74
const val QUALITY_720P = 64
const val QUALITY_480P = 32
const val QUALITY_360P = 16
const val QUALITY_240P = 6
