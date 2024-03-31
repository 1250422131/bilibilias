package com.imcys.bilibilias.home.ui.fragment.tool

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState
    data object EmptyQuery : SearchResultUiState

    data object LoadFailed : SearchResultUiState
    data class Success(
        val aid: Long,
        val bvid: String,
        val cid: Long,
        val collection: List<View>
    ) : SearchResultUiState
}

data class View(val cid: Long, val title: String)
