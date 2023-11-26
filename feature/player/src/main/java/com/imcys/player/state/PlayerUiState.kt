package com.imcys.player.state

import com.imcys.model.Dash
import com.imcys.model.video.PageData
import kotlinx.collections.immutable.ImmutableList

sealed interface PlayerUiState {
    data object Loading : PlayerUiState
    data object LoadFailed : PlayerUiState
    data class Success(
        val qualityDescription: ImmutableList<Pair<String, Int>>,
        val video: ImmutableList<Dash.Video>,
        val audio: ImmutableList<Dash.Audio>,
        val dolby: Dash.Dolby?,
        val duration: Int,
        val pageData: List<PageData>
    ) : PlayerUiState
}
