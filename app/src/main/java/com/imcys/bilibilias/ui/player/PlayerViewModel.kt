package com.imcys.bilibilias.ui.player

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.imcys.bilibilias.ui.download.DownloadOptionsStateHolders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
) : BaseViewModel() {

    private val _event = MutableStateFlow(PlayerState())
    val event = _event.asStateFlow()

    val downloadOptions by lazy(LazyThreadSafetyMode.NONE) {
        DownloadOptionsStateHolders().apply {
            videoFormatDescription = _event.value.dashVideo.acceptDescription.first()
            videoQuality = _event.value.dashVideo.supportFormats.first().quality
            subset.add(_event.value.videoDetails.pages.first())
            audioFormatDescription = _event.value.dashVideo.acceptQuality.first()
            _event.value.dashVideo.dash.audio.first().id
        }
    }

    init {
        videoRepository.videoDetails.onEach { v ->
            // val dash = videoRepository.getDashVideoStream(v.bvid, v.cid).collect{
            //     when (it) {
            //         is Result.Error -> TODO()
            //         Result.Loading -> TODO()
            //         is Result.Success -> TODO()
            //     }
            // }
            // val hasLike = videoRepository.hasLike(v.bvid).like
            // val hasCoins = videoRepository.hasCoins(v.bvid).coins
            // val hasCollection = videoRepository.hasCollection(v.bvid).isFavoured
            // _event.update {
            //     it.copy(
            //         videoDetails = v,
            //         dashVideo = dash,
            //         hasLike = hasLike,
            //         hasCoins = hasCoins,
            //         hasCollection = hasCollection
            //     )
            // }
        }.launchIn(viewModelScope)
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
        launchIO {
            // val play = videoRepository.getMP4VideoStream(_event.value.videoDetails.bvid, cid)
            // _event.update {
            //     it.copy(
            //         videoPlayDetails = play,
            //     )
            // }
        }
    }

}

data class PlayerState(
    val videoDetails: VideoDetails = VideoDetails(),
    val hasLike: Boolean = false,
    val hasCoins: Boolean = false,
    val hasCollection: Boolean = false,
    val pageList: List<VideoPageListData> = emptyList(),
    val dashVideo: DashVideoPlayBean = DashVideoPlayBean()
)
