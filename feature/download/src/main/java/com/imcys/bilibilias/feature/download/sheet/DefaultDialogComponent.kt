package com.imcys.bilibilias.feature.download.sheet

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultDialogComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val onDismissed: () -> Unit
) : DialogComponent, BaseViewModel<Unit, Unit>(componentContext) {
    @Composable
    override fun models(events: Flow<Unit>) {
        TODO("Not yet implemented")
    }

    override fun onDismissClicked() {
        onDismissed ()
    }
}

