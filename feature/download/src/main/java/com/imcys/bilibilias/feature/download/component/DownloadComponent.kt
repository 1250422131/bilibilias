package com.imcys.bilibilias.feature.download.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import com.imcys.bilibilias.feature.player.PlayerComponent
import kotlinx.coroutines.flow.StateFlow

interface DownloadComponent {
    val models: StateFlow<Model>

    val stack: Value<ChildStack<*, Child>>
    val dialogSlot: Value<ChildSlot<*, DialogComponent>>

    fun take(event: Event)
    fun onSettingsClicked(info: ViewInfo, fileType: FileType)
    fun onPlayerClicked()

    sealed class Child {
        data object DownloadChild : Child()
        data class PlayerChild(val component: PlayerComponent) : Child()
    }

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DownloadComponent
    }
}
