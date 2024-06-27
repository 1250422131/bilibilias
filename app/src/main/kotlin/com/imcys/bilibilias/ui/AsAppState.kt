package com.imcys.bilibilias.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.hjq.toast.Toaster
import com.imcys.bilibilias.R
import com.imcys.bilibilias.core.common.utils.getActivity
import com.imcys.bilibilias.core.data.toast.ToastMachine
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.ui.TrackDisposableJank
import com.imcys.bilibilias.navigation.RootComponent
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KFunction0

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
internal fun NavigationTrackingSideEffect(child: RootComponent.Child) {
    TrackDisposableJank(child) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", child.toString())
        onDispose {}
    }
}

@Composable
internal fun AsBackHandler(
    backHandler: BackHandler,
    currentTopLevelDestination: TopLevelDestination?,
    currentDestination: RootComponent.Child,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    var exitTime by remember { mutableLongStateOf(0) }

    BackHandler(backHandler) {
        if (currentTopLevelDestination != null || currentDestination is RootComponent.Child.LoginChild) {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - exitTime > 2000) {
                Toaster.show(R.string.app_HomeActivity_exit)
                exitTime = currentTimeMillis
            } else {
                context.getActivity().finish()
            }
        } else onBack()
    }
}

@Composable
internal fun BackHandler(backHandler: BackHandler, isEnabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)

    val callback =
        remember {
            BackCallback(isEnabled = isEnabled) {
                currentOnBack()
            }
        }

    SideEffect { callback.isEnabled = isEnabled }

    DisposableEffect(backHandler) {
        backHandler.register(callback)
        onDispose { backHandler.unregister(callback) }
    }
}
