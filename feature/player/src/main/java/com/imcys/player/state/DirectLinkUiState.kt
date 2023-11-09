package com.imcys.player.state

import com.imcys.model.Dash

sealed interface DirectLinkUiState {
    data object Empty : DirectLinkUiState
    data class Success(
        val video: Dash.Video,
        val audio: Dash.Audio,
        val dolby: Dash.Dolby?,
    ) : DirectLinkUiState
}
