package com.imcys.space

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails
import com.imcys.network.repository.user.GetUserSubmittedVideoPagingSource
import com.imcys.network.repository.user.IUserDataSources
import com.imcys.network.repository.video.IVideoDataSources
import com.imcys.space.navigation.MID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class UserSpaceViewModel @Inject constructor(
    private val userRepository: IUserDataSources,
    savedStateHandle: SavedStateHandle,
    private val getVideoInformationUseCase: GetVideoInformationUseCase
) : ViewModel() {
    private val mid = savedStateHandle.getStateFlow(MID, 0L)
    val spaceArcSearchPagingSource = Pager(
        PagingConfig(pageSize = 20),
    ) {
        GetUserSubmittedVideoPagingSource(userRepository, mid.value)
    }
        .flow
        .cachedIn(viewModelScope)

    fun getVideoInformation(bvid: String) {
       getVideoInformationUseCase(bvid)
    }
}

class GetVideoInformationUseCase @Inject constructor(
    private val videoRepository: IVideoDataSources
) {
    operator fun invoke(bvid: String): Flow<VideoInformation> = flow {
        val videoDetails = videoRepository.getDetail(bvid)
        val playerPlayUrl = videoRepository.getPlayerPlayUrl(bvid, videoDetails.cid)
        emit(VideoInformation(videoDetails, playerPlayUrl))
    }
}

data class VideoInformation(val videoDetails: VideoDetails, val playerPlayUrl: PlayerInfo)