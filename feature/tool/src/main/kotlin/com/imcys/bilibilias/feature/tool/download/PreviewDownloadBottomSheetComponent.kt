package com.imcys.bilibilias.feature.tool.download

import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDownloadBottomSheetComponent : DownloadBottomSheetComponent {
    override val models: StateFlow<Model>
        get() = MutableStateFlow(
            Model(
                title = "",
                pagePart = "",
                author = "",
                format = listOf(
                    Format(
                        formatId = 0,
                        url = "",
                        codecId = 0,
                        codecs = "",
                        mimeType = "",
                        bandwidth = 0,
                        duration = 0,
                    ),
                ),
            ),
        )

    override fun take(event: DownloadTypeBottomSheetEvent) {
    }
}
