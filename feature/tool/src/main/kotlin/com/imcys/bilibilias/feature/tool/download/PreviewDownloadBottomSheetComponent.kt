package com.imcys.bilibilias.feature.tool.download

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDownloadBottomSheetComponent : DownloadBottomSheetComponent {
    override val model: StateFlow<DownloadBottomSheetComponent.Model>
        get() = MutableStateFlow(DownloadBottomSheetComponent.Model)
}
