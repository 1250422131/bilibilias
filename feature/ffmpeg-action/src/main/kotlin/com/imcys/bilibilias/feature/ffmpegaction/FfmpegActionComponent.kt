package com.imcys.bilibilias.feature.ffmpegaction

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface FfmpegActionComponent {
    val models: StateFlow<Unit>
    fun take(event: Unit)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): FfmpegActionComponent
    }
}
