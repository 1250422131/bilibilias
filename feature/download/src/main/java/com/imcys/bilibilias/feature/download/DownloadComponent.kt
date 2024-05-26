package com.imcys.bilibilias.feature.download

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import kotlinx.coroutines.flow.StateFlow

interface DownloadComponent {
    val models: StateFlow<Model>
    val dialogSlot: Value<ChildSlot<*, DialogComponent>>
    fun take(event: Event)
    fun onSettingsClicked()

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DownloadComponent
    }
}
