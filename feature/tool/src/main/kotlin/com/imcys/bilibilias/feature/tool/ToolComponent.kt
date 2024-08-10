package com.imcys.bilibilias.feature.tool

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.download.DownloadRequest
import kotlinx.coroutines.flow.StateFlow

interface ToolComponent {
    val searchQuery: StateFlow<String>
    val searchResultUiState: StateFlow<SearchResultUiState>
    fun download(request: DownloadRequest)
    fun onSearchQueryChanged(query: String)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): ToolComponent
    }
}
