package com.imcys.player.state

import com.imcys.model.VideoDetails
import com.imcys.model.video.Page

sealed interface PlayInfoUiState {
    data object Loading : PlayInfoUiState
    data object LoadFailed : PlayInfoUiState
    data class Success(
        val aid: Long,
        val bvid: String,
        val cid: Long,
        val title: String,
        val pic: String,
        val desc: String,
        val pages: List<Page>,
        val stat: VideoDetails.Stat,
    ) : PlayInfoUiState
}
