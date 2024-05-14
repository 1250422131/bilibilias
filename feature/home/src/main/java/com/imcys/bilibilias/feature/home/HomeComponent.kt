package com.imcys.bilibilias.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.IComponentContext

interface HomeComponent : IComponentContext<HomeEvent, HomeUiState> {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): HomeComponent
    }
}
