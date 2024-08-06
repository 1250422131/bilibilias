package com.imcys.bilibilias.feature.common

import androidx.compose.runtime.Composable
import app.cash.molecule.AndroidUiDispatcher
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<Event, Model>(componentContext: ComponentContext) : ComponentContext by componentContext {
    val viewModelScope = CoroutineScope(AndroidUiDispatcher.Main)

    protected val moleculeScope = CoroutineScope(AndroidUiDispatcher.Main)

    protected open val recompositionMode = RecompositionMode.ContextClock

    // Events have a capacity large enough to handle simultaneous UI events, but
    // small enough to surface issues if they get backed up for some reason.
    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)

    open val models: StateFlow<Model> by lazy(LazyThreadSafetyMode.NONE) {
        moleculeScope.launchMolecule(mode = recompositionMode) {
            models(events)
        }
    }

    fun take(event: Event) {
        if (!events.tryEmit(event)) {
            error("Event buffer overflow.")
        }
    }

    @Composable
    protected abstract fun models(events: Flow<Event>): Model
}
