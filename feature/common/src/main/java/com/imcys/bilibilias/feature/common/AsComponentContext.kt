package com.imcys.bilibilias.feature.common

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.molecule.AndroidUiDispatcher
import com.imcys.bilibilias.feature.common.molecule.RecompositionMode
import com.imcys.bilibilias.feature.common.molecule.launchMolecule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
@Deprecated("")
abstract class AsComponentContext2(componentContext: ComponentContext) :
    ComponentContext by componentContext {
    protected val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}

abstract class AsComponentContext<Event, Model>(
    componentContext: ComponentContext
) : AsComponentContext2(componentContext) {
    private val moleculeScope = CoroutineScope(AndroidUiDispatcher.Main)
    protected val scope2 = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    // Events have a capacity large enough to handle simultaneous UI events, but
    // small enough to surface issues if they get backed up for some reason.
    private val events = MutableSharedFlow<Event>(extraBufferCapacity = 20)

    val models: StateFlow<Model> by lazy(LazyThreadSafetyMode.NONE) {
        moleculeScope.launchMolecule(mode = RecompositionMode.ContextClock) {
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
