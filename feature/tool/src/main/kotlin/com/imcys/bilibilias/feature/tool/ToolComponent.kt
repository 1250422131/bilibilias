package com.imcys.bilibilias.feature.tool

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.feature.tool.DefaultToolComponent.Config
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent
import kotlinx.coroutines.flow.StateFlow

interface ToolComponent {
    val searchQuery: StateFlow<String>
    val searchResultUiState: StateFlow<SearchResultUiState>
    val dialogSlot: Value<ChildSlot<*, DownloadBottomSheetComponent>>
    fun download(request: DownloadRequest)
    fun onSearchQueryChanged(query: String)
    fun navigationTioDownloadTypeBottomSheet()
    fun onDismissClicked()
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): ToolComponent
    }
}
