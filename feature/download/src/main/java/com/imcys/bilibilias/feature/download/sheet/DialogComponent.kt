package com.imcys.bilibilias.feature.download.sheet

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo

interface DialogComponent {
    fun onDismissClicked()
    fun take(event: Event)
    sealed interface Event {
        data class DeleteFile(val viewInfo: ViewInfo, val fileType: FileType) : Event
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            info: ViewInfo,
            fileType: FileType,
            onDismissed: () -> Unit
        ): DialogComponent
    }
}
