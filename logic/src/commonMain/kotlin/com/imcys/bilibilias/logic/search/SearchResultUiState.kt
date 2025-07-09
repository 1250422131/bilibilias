package com.imcys.bilibilias.logic.search

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
        val aid: Long,
        val bvid: String,
        val desc: String,
        val cover: String,
        val title: String,
        val ownerId: Long,
        val ownerFace: String,
        val ownerName: String,
        val episodes: List<Episode> = emptyList(),
        val availableQualities: List<EpisodeQuality> = emptyList()
    ) : SearchResultUiState

    data class Error(val message: String) : SearchResultUiState
}