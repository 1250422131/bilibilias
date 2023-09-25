package com.imcys.bilibilias.ui.player

import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.model.video.VideoAllDetails
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoPlayDetails
import com.imcys.bilibilias.common.base.repository.VideoRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(private val videoRepository: VideoRepository) : BaseViewModel() {

    private val _event = MutableStateFlow(PlayerSate())
    val event = _event.asStateFlow()

    private val videoDetails = videoRepository.videoDetailsFlow
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = VideoDetails())

    private val videoPlayDetails = videoRepository.videoPlayDetailsFlow
        .stateIn(scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = VideoPlayDetails())

    val videoAllDetailsState =
        combine(
            videoDetails,
            videoPlayDetails,
            videoRepository.isLikeFlow,
            videoRepository.isCoinsFlow,
            videoRepository.isCollectionFlow,
            ::VideoAllDetails
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            VideoAllDetails(
                videoDetails.value,
                videoPlayDetails.value,
                isLike = false,
                isCoins = false,
                isCollection = false
            )
        )

    // init {
    //     videoRepository.testSharedFlow.onEach { v ->
    //         val m = videoRepository.getMP4VideoStream(v.bvid, v.cid)
    //         val hasLike = videoRepository.hasLike(v.bvid).like
    //         val hasCoins = videoRepository.hasCoins(v.bvid).coins
    //         val hasCollection = videoRepository.hasCollection(v.bvid).isFavoured
    //         _event.update {
    //             it.copy(
    //                 videoDetails = v,
    //                 videoPlayDetails = m,
    //                 hasLike = hasLike,
    //                 hasCoins = hasCoins,
    //                 hasCollection = hasCollection
    //             )
    //         }
    //     }.launchIn(viewModelScope)
    // }
}

data class PlayerSate(
    val videoDetails: VideoDetails = VideoDetails(),
    val videoPlayDetails: VideoPlayDetails = VideoPlayDetails(),
    val hasLike: Boolean = false,
    val hasCoins: Boolean = false,
    val hasCollection: Boolean = false,
)
