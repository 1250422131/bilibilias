package com.imcys.bilibilias.logic.search

import com.imcys.bilibilias.core.data.model.EpisodeCacheListState
import com.imcys.bilibilias.core.data.model.EpisodeCacheState
import com.imcys.bilibilias.core.data.model.EpisodeInfo2

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState

    /**
     * The state query is empty or too short. To distinguish the state between the
     * (initial state or when the search query is cleared) vs the state where no search
     * result is returned, explicitly define the empty query state.
     */
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState

    data class Success(
        val episodeInfo: EpisodeInfo2,
        val episodes: List<EpisodeCacheState>,
        val episodeCacheListState: EpisodeCacheListState,
        val isGuestUser: Boolean,
    ) : SearchResultUiState

    data class Error(val message: String) : SearchResultUiState
}

sealed interface EpisodeListUiState {
    data class Loading(val episodes: List<EpisodeCacheState> = emptyList()) :
        EpisodeListUiState // Potentially show cached while loading new

    data class Success(val episodes: List<EpisodeCacheState>) : EpisodeListUiState
    data class Error(val message: String) : EpisodeListUiState
}