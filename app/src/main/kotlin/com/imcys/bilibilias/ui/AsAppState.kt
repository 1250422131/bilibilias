package com.imcys.bilibilias.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.ui.TrackDisposableJank
import com.imcys.bilibilias.navigation.RootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberNiaAppState(
    toastMachine: ToastMachine,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AsAppState {
    return remember(
        toastMachine,
        networkMonitor,
        coroutineScope,
    ) {
        AsAppState(
            toastMachine = toastMachine,
            networkMonitor = networkMonitor,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class AsAppState(
    toastMachine: ToastMachine,
    val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
) {
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
    val message = toastMachine.message.stateIn(
        coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
internal fun NavigationTrackingSideEffect(component: RootComponent) {
    val stack by component.stack.subscribeAsState()
    val action = stack.active
    TrackDisposableJank(action) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", action.toString())
        onDispose {}
    }
}
