package com.imcys.bilibilias.feature.download.sheet

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo

interface DialogComponent {
    fun onDismissClicked()
    fun onNavigationToPlayer()

    fun take(event: Event)

    sealed interface Event {
        data object DeleteFile : Event
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            info: ViewInfo,
            fileType: FileType,
            onDismissed: () -> Unit,
            onNavigationToPlayer: () -> Unit,
        ): DialogComponent
    }
}
