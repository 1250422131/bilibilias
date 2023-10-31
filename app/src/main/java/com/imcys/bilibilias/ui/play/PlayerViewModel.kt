package com.imcys.bilibilias.ui.play

import androidx.annotation.OptIn
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.mediacodec.MediaCodecInfo
import androidx.media3.exoplayer.mediacodec.MediaCodecUtil
import com.imcys.network.DownloadManage
import com.imcys.common.utils.Result
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.network.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.imcys.bilibilias.ui.download.DownloadListHolders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: com.imcys.network.VideoRepository,
    val downloadListHolders: DownloadListHolders,
    private val downloadManage: com.imcys.network.DownloadManage
) : BaseViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    /**
     * key 是视频清晰度
     * 126,120,112,80,64,32,16
     */
    private val qualityGroup = sortedMapOf<Int, List<com.imcys.model.Dash.Video>>()

    /**
     * 视频支持的格式
     */
    private val supportFormat = mutableListOf<com.imcys.model.SupportFormat>()

    private val pages = mutableListOf<com.imcys.model.VideoDetails.Page>()

    init {
        getMediaCodecInfo()
        viewModelScope.launchIO {
            videoRepository.videoDetails2.collectLatest { res ->
                when (res) {
                    is com.imcys.common.utils.Result.Error -> TODO()
                    com.imcys.common.utils.Result.Loading -> TODO()
                    is com.imcys.common.utils.Result.Success -> {
                        val details = res.data
                        getDash(details)
                        setSubSet(details.pages)
                        downloadDanmaku(details.cid, details.aid)
                        val hasLike = videoRepository.hasLike(details.bvid)
                        val hasCoins = videoRepository.hasCoins(details.bvid)
                        val hasCollection = videoRepository.hasCollection(details.bvid)
                        setVideoBasicInfo(details)
                        _playerState.update {
                            it.copy(
                                hasLike = hasLike,
                                hasCoins = hasCoins,
                                hasCollection = hasCollection
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setVideoBasicInfo(details: com.imcys.model.VideoDetails) {
        _playerState.update {
            it.copy(
                title = details.title,
                desc = details.descV2?.first()?.rawText ?: details.desc,
                bvid = details.bvid,
                aid = details.aid,
                cid = details.cid,
                pic = details.pic,
                like = details.stat.like,
                coins = details.stat.coin,
                favorite = details.stat.favorite,
                share = details.stat.share
            )
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
    fun selectedPage(cid: Long, aid: Long) {
        val bvid = _playerState.value.bvid
        launchOnIO {
            when (val res = videoRepository.getDashVideoStream(bvid, cid, useWbi = true)) {
                is com.imcys.common.utils.Result.Error -> TODO()
                com.imcys.common.utils.Result.Loading -> TODO()
                is com.imcys.common.utils.Result.Success -> {
                    setQualityGroup(res.data.dash.video)
                    val qn = _playerState.value.currentQn
                    selectedQuality(qn)
                    _playerState.update { state ->
                        state.copy(cid = cid, audio = res.data.dash.audio.maxBy { it.id })
                    }
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    fun getMimeType(codec: String) = MimeTypes.getMediaMimeType(codec)

    @OptIn(UnstableApi::class)
    fun getDecoderInfo(mimeType: String) = MediaCodecUtil.getDecoderInfo(mimeType, false, false)

    private fun downloadDanmaku(cid: Long, aid: Long) {
        downloadManage.downloadDanmaku(cid, aid)
    }

    private fun setSubSet(pages: List<com.imcys.model.VideoDetails.Page>) {
        downloadListHolders.subset.add(pages.first())
    }

    private suspend fun getDash(details: com.imcys.model.VideoDetails) {
        when (val res = videoRepository.getDashVideoStream(details.bvid, details.cid)) {
            is com.imcys.common.utils.Result.Error -> TODO()
            com.imcys.common.utils.Result.Loading -> TODO()
            is com.imcys.common.utils.Result.Success -> {
                val data = res.data
                val videos = data.dash.video
                setQualityGroup(videos)
                selectedQuality(videos.画质最高队列())

                supportFormat.clear()
                supportFormat.addAll(data.supportFormats)

                pages.clear()
                pages.addAll(details.pages)
                _playerState.update { state ->
                    state.copy(
                        audio = data.dash.audio.maxBy { it.id },
                        descriptionAndQuality = data.acceptDescription.zip(data.acceptQuality),
                        pages = details.pages,
                        cid = details.pages.first().cid
                    )
                }
            }
        }
    }

    private fun setQualityGroup(videos: List<com.imcys.model.Dash.Video>) {
        qualityGroup.clear()
        qualityGroup.putAll(videos.画质分组())
    }

    private fun selectedQuality(videoList: List<com.imcys.model.Dash.Video>) {
        for (v in videoList) {
            if (selectDecoder(v.codecs) != null) {
                _playerState.update { state -> state.copy(video = v) }
                break
            }
        }
    }
}

@Stable
data class PlayerState(
    val title: String = "",
    val desc: String = "",
    val bvid: String = "",
    val aid: Long = 0,
    val cid: Long = 0,
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
    val pages: List<com.imcys.model.VideoDetails.Page> = emptyList(),

    val currentQn: Int = 0,

    val video: com.imcys.model.Dash.Video = com.imcys.model.Dash.Video(),
    val audio: com.imcys.model.Dash.Audio = com.imcys.model.Dash.Audio(),
)

fun List<com.imcys.model.Dash.Video>.画质分组(): Map<Int, List<com.imcys.model.Dash.Video>> = groupBy { it.id }

fun List<com.imcys.model.Dash.Video>.画质最高队列(): List<com.imcys.model.Dash.Video> =
    groupBy { it.id }
        .maxBy { it.key }
        .value
        .sortedBy { it.codecid }
