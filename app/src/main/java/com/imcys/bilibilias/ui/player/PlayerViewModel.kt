package com.imcys.bilibilias.ui.player

import android.media.MediaCodecList
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MimeTypes
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.model.video.Dash
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.video.SupportFormat
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.imcys.bilibilias.ui.download.DownloadListHolders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    val downloadListHolders: DownloadListHolders,
    okHttpClient: OkHttpClient
) : BaseViewModel() {

    private val _playerState = MutableStateFlow(PlayerState(okHttpClient = okHttpClient))
    val playerState = _playerState.asStateFlow()

    /**
     * key 是视频清晰度
     * 126,120,112,80,64,32,16
     */
    private val qualityGroup = sortedMapOf<Int, List<Dash.Video>>()

    /**
     * 视频支持的格式
     */
    private val supportFormat = mutableListOf<SupportFormat>()

    init {
        getMediaCodecInfo()
        viewModelScope.launchIO {
            videoRepository.videoDetails2.collectLatest { res ->
                when (res) {
                    is Result.Error -> TODO()
                    Result.Loading -> TODO()
                    is Result.Success -> {
                        val details = res.data
                        getDash(details)
                        setSubSet(details.pages)
                        val hasLike = videoRepository.hasLike(details.bvid)
                        val hasCoins = videoRepository.hasCoins(details.bvid)
                        val hasCollection = videoRepository.hasCollection(details.bvid)
                        _playerState.update {
                            it.copy(
                                videoDetails = details,
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

    private fun setSubSet(pages: List<VideoDetails.Page>) {
        downloadListHolders.subset.add(pages.first())
    }

    private suspend fun getDash(details: VideoDetails) {
        when (val res = videoRepository.getDashVideoStream(details.bvid, details.cid)) {
            is Result.Error -> TODO()
            Result.Loading -> TODO()
            is Result.Success -> {
                val data = res.data
                val videos = data.dash.video
                _playerState.update { state ->
                    Timber.tag("PlayerViewModel").d(videos.toString())
                    val descriptionAndQuality = data.acceptDescription.zip(data.acceptQuality)
                    state.copy(
                        dashVideo = data,
                        audio = data.dash.audio.maxBy { it.id },
                        videos = videos,
                        descriptionAndQuality = descriptionAndQuality
                    )
                }
                qualityGroup.clear()
                supportFormat.clear()

                supportFormat.addAll(data.supportFormats)
                qualityGroup.putAll(videos.画质分组())
            }
        }
    }

    // 选择视频清晰度
    // todo 是否还需要查看手机支持的编码器，以选择合适的 url？
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun selectedQuality(quality: Int) {
        Timber.d("qn=$quality")
        val mediaCodecInfos = get软解()

        val videoList = qualityGroup[quality]
        val format = supportFormat.find { it.quality == quality } ?: supportFormat.maxBy { it.quality }

        if (videoList == null) {
            _playerState.update { state ->
                state.copy(video = qualityGroup.maxBy { it.key }.value.maxBy { it.codecid })
            }
        } else {
            _playerState.update { state ->
                state.copy(video = videoList.maxBy { it.codecid })
            }
            // val codes = format.codecs.map { MimeTypes.getMediaMimeType(it)?.drop(6) ?: "" }
            // mediaCodecInfos.forEach { media ->
            //     codes.forEach { code ->
            //         if (media.contains(code)) {
            //             Timber.d("media=$media,code=$code")
            //             videoList.forEach {
            //                 if (it.codecs.contains(code)) {
            //                     _playerState.update { state ->
            //                         state.copy(video = it)
            //                     }
            //                 }
            //             }
            //         }
            //     }
            // }
        }
    }

    fun getVideoPlayList(bvid: String) {
        // launchIO {
        //     val pageList = videoRepository.getVideoPageList(bvid)
        //     _event.update {
        //         it.copy(pageList = pageList)
        //     }
        // }
    }

    // 切换page
    fun switchPage(cid: Long) {

    }

    fun changeUrl(cid: Long) {
    }
}

data class PlayerState(
    val videoDetails: VideoDetails = VideoDetails(),
    val hasLike: Boolean = false,
    val hasCoins: Boolean = false,
    val hasCollection: Boolean = false,
    val dashVideo: DashVideoPlayBean = DashVideoPlayBean(),
    val videos: List<Dash.Video> = emptyList(),

    val okHttpClient: OkHttpClient,
    val descriptionAndQuality: List<Pair<String, Int>> = emptyList(),

    // 清晰度
    val video: Dash.Video = Dash.Video(),
    val audio: Dash.Audio = Dash.Audio(),
)

fun get软解(): List<String> {
    val list = MediaCodecList(MediaCodecList.REGULAR_CODECS)
    val supportCodes = list.codecInfos
    val codecInfoList = supportCodes.filterNot { it.isEncoder }.filter { it.name.startsWith("OMX.google") }.map { it.name }
    codecInfoList.forEach {
        Timber.i("软解列表：${it}\n")
    }
    return codecInfoList

}

fun List<Dash.Video>.画质分组(): Map<Int, List<Dash.Video>> = groupBy { it.id }

fun List<Dash.Video>.画质最高队列(): List<Dash.Video> =
    groupBy { it.id }
        .maxBy { it.key }
        .value
        .sortedBy { it.codecid }

fun List<Dash.Video>.画质最高队列的高效编码(): Dash.Video =
    画质最高队列().maxBy { it.codecid }
