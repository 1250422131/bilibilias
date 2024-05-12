package com.imcys.bilibilias.feature.tool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.core.download.DownloadRequest

@Composable
fun ToolRoute(
    onSetting: () -> Unit,
    onPlayer: () -> Unit
) {
    val viewmodel: ToolViewModel = hiltViewModel()
    val searchQuery by viewmodel.searchQuery.collectAsState()
    val uiState by viewmodel.searchResultUiState.collectAsState()
    ToolScreen(
        searchQuery,
        uiState,
        viewmodel::clearSearches,
        viewmodel::onSearchQueryChanged,
        viewmodel::download,
        onSetting,
    )
}

@Composable
internal fun ToolScreen(
    searchQuery: String,
    uiState: SearchResultUiState,
    clearSearches: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onDownload: (DownloadRequest) -> Unit,
    onSetting: () -> Unit
) {
    ToolContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = onSearchQueryChanged,
        onClearSearches = clearSearches,
        searchResultUiState = uiState,
        onDownload = onDownload,
        onSetting,
    )
}
