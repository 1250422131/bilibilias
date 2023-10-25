package com.imcys.bilibilias.ui.player

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.launchIO
import com.imcys.bilibilias.common.base.model.video.Dash
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
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
            is Result.Success -> _playerState.update {
                Timber.tag("PlayerViewModel").d(res.data.dash.video.toString())
                it.copy(dashVideo = res.data, audios = res.data.dash.audio, videos = res.data.dash.video)
            }
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
    val audios: List<Dash.Audio> = emptyList(),
    val okHttpClient: OkHttpClient
)
fun List<Dash.Video>.画质分组(): Map<Int, List<Dash.Video>> = groupBy { it.id }

fun List<Dash.Video>.画质最高队列(): List<Dash.Video> =
    groupBy { it.id }
        .maxBy { it.key }
        .value
        .sortedBy { it.codecid }

fun List<Dash.Video>.画质最高队列的高效编码(): Dash.Video =
    画质最高队列().maxBy { it.codecid }
