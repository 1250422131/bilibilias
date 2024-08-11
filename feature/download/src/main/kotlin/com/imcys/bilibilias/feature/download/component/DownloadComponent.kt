package com.imcys.bilibilias.feature.download.component

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface DownloadComponent {
    val models: StateFlow<Model>

    val selectedDeletes: List<Int>

    fun take(event: Event)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DownloadComponent
    }
}
