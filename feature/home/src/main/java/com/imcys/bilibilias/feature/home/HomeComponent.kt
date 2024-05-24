package com.imcys.bilibilias.feature.home

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val models: StateFlow<HomeUiState>
    fun take(event: HomeEvent)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): HomeComponent
    }
}
