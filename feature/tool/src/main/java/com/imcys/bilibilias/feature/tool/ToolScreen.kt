package com.imcys.bilibilias.feature.tool

import SearchResultUiState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
internal fun ToolRoute() {
    val viewmodel: ToolViewModel = hiltViewModel()
    val searchQuery by viewmodel.searchQuery.collectAsState()
    val uiState by viewmodel.searchResultUiState.collectAsState()
    ToolScreen(searchQuery, uiState, viewmodel::clearSearches, viewmodel::onSearchQueryChanged)
}

@Composable
internal fun ToolScreen(
    searchQuery: String,
    uiState: SearchResultUiState,
    clearSearches: () -> Unit,
    onSearchQueryChanged: (String) -> Unit
) {
    ToolContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        onClearSearches = clearSearches,
        onDownloadFile = {},
        searchResultUiState = uiState
    )
}