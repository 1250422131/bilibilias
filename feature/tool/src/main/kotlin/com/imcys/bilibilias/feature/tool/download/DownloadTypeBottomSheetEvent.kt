package com.imcys.bilibilias.feature.tool.download

sealed interface DownloadTypeBottomSheetEvent {
    data class ChangesTitle(val text: String) : DownloadTypeBottomSheetEvent
    data class ChangesAuthor(val text: String) : DownloadTypeBottomSheetEvent
}
