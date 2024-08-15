package com.imcys.bilibilias.feature.tool.download

import kotlinx.coroutines.flow.StateFlow

interface DownloadBottomSheetComponent {
    val model: StateFlow<Model>

    object Model
}
