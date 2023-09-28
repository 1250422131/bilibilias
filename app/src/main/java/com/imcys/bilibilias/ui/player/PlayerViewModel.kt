package com.imcys.bilibilias.ui.player

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.common.base.model.video.VideoPlayDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import com.imcys.bilibilias.ui.download.DownloadOptionsStateHolders
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
            videoClarity = _event.value.videoPlayDetails.acceptDescription.first()
            cachedSubset.add(_event.value.videoDetails.pages.first())
            cacheType = "Dash"
            cachedAudioQuality = _event.value.videoPlayDetails.acceptQuality.first()
        }
    }

    init {
        videoRepository.videoPlayDetailsFlow.onEach { p ->
            _event.update {
                it.copy(videoPlayDetails = p)
            }
        }.launchIn(viewModelScope)
        videoRepository.videoDetailsFlow.onEach { v ->
            val m = videoRepository.getMP4VideoStream(v.bvid, v.cid)
            val hasLike = videoRepository.hasLike(v.bvid).like
            val hasCoins = videoRepository.hasCoins(v.bvid).coins
            val hasCollection = videoRepository.hasCollection(v.bvid).isFavoured
            _event.update {
                it.copy(
                    videoDetails = v,
                    videoPlayDetails = m,
                    hasLike = hasLike,
                    hasCoins = hasCoins,
                    hasCollection = hasCollection
                )
            }
        }.launchIn(viewModelScope)
    }

    fun getVideoPlayList(bvid: String) {
        launchIO {
            val pageList = videoRepository.getVideoPageList(bvid)
            _event.update {
                it.copy(pageList = pageList)
            }
        }
    }

    fun changeUrl(cid: Long) {
        launchIO {
            val play = videoRepository.getMP4VideoStream(_event.value.videoDetails.bvid, cid)
            _event.update {
                it.copy(
                    videoPlayDetails = play,
                )
            }
        }
    }

    /**
     * 缓存视频
     */
    fun downloadVideo() {
        launchIO {
            videoRepository.getDashVideoStream(
                _event.value.videoDetails.bvid,
                _event.value.videoDetails.cid,
                64,
                4048,
                1
            )
        }
        // if ((context as AsVideoActivity).userSpaceInformation.level >= 2) {
        // videoRepository.getDashVideoStream(videoDetails.bvid, v)
        /*  if (dashVideoPlayBean.videoCodecid == 0) {
         */
        /*  DialogUtils.downloadVideoDialog(
                  context,
                  videoDetails,
                  videoPageListData,
                  dashVideoPlayBean,
              ).show()*/
        /*
          }*/

        /*    } else {
                launchUI {
                    AsDialog.init(context).build {
                        config = {
                            title = "止步于此"
                            content =
                                "鉴于你的账户未转正，请前往B站完成答题，否则无法为您提供缓存服务。\n" +
                                        "作者也是B站UP主，见到了许多盗取视频现象，更有甚者缓存番剧后发布内容到其他平台。\n" +
                                        "而你的账户甚至是没有转正的，bilibilias自然不会想提供服务。"
                            positiveButtonText = "知道了"
                            positiveButton = {
                                it.cancel()
                            }
                        }
                    }.show()
                }*/
    }
}

data class PlayerState(
    val videoDetails: VideoDetails = VideoDetails(),
    val videoPlayDetails: VideoPlayDetails = VideoPlayDetails(),
    val bangumi: Bangumi = Bangumi(),
    val hasLike: Boolean = false,
    val hasCoins: Boolean = false,
    val hasCollection: Boolean = false,
    val pageList: List<VideoPageListData> = emptyList(),
)
