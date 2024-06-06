package com.imbys.bilibilias.feature.authorspace

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultAuthorSpaceComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : AuthorSpaceComponent, BaseViewModel<Unit, Unit>(componentContext) {
    @Composable
    override fun models(events: Flow<Unit>) {
    }

    @AssistedFactory
    interface Factory : AuthorSpaceComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultAuthorSpaceComponent
    }
}
