package com.imcys.bilibilias.feature.download.sheet

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import kotlinx.coroutines.flow.StateFlow

interface DialogComponent {
    val models: StateFlow<Unit>

    fun onDismissClicked()
    fun navigationToPlayer(): ViewInfo

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
        ): DialogComponent
    }
}
