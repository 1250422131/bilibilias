package com.imcys.bilibilias.feature.tool.download

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.ViewIds
import kotlinx.coroutines.flow.StateFlow

interface DownloadBottomSheetComponent {
    val models: StateFlow<Model>
    fun take(event: DownloadTypeBottomSheetEvent)
    data class Model(
        val title: String,
        val pagePart: String,
        val author: String,
        val format: List<Format>,
    )

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            title: String,
            author: String,
            id: ViewIds,
        ): DownloadBottomSheetComponent
    }
}

data class Format(
    val formatId: Int,
    val url: String,
    val codecId: Int,
    val codecs: String,

    val mimeType: String,
    val bandwidth: Int,
    val duration: Int,
    val width: Int = 0,
    val height: Int = 0,

    val sar: String = "",
    val fps: String = "",
)
