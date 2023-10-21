package com.imcys.bilibilias.ui.player

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.launchIO
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
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    val downloadListHolders: DownloadListHolders
) : BaseViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    init {
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
        videoRepository.getDashVideoStream(details.bvid, details.cid)
        videoRepository.dashVideo.replayCache.forEach { res ->
            when (res) {
                is Result.Error -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> _playerState.update {
                    it.copy(dashVideo = res.data)
                }
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
)
