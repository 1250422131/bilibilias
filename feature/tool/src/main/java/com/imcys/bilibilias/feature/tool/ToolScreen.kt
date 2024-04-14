package com.imcys.bilibilias.feature.tool

import DownloadFileRequest
import SearchResultUiState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen

class ToolScreen : Screen {
    @Composable
    override fun Content() {
        ToolRoute()
    }
}

@Composable
internal fun ToolRoute() {
    val viewmodel: ToolViewModel = hiltViewModel()
    val searchQuery by viewmodel.searchQuery.collectAsState()
    val uiState by viewmodel.searchResultUiState.collectAsState()
    ToolScreen(
        searchQuery,
        uiState,
        viewmodel::clearSearches,
        viewmodel::onSearchQueryChanged,
        viewmodel::download
    )
}

@Composable
internal fun ToolScreen(
    searchQuery: String,
    uiState: SearchResultUiState,
    clearSearches: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    download: (DownloadFileRequest) -> Unit
) {
    ToolContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        onClearSearches = clearSearches,
        searchResultUiState = uiState,
        onDownload = download
    )
}
