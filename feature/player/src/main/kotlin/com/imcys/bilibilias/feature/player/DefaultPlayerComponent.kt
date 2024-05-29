package com.imcys.bilibilias.feature.player

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultPlayerComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : PlayerComponent, BaseViewModel<Unit, Unit>(componentContext) {

    @Composable
    override fun models(events: Flow<Unit>) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory : PlayerComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultPlayerComponent
    }
}
