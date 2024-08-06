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
import com.imcys.bilibilias.core.data.util.ErrorMessage
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.ui.TrackDisposableJank
import com.imcys.bilibilias.core.utils.getActivity
import com.imcys.bilibilias.navigation.RootComponent
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAsAppState(
    errorMonitor: ErrorMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AsAppState = remember(
    errorMonitor,
    coroutineScope,
) {
    AsAppState(
        errorMonitor = errorMonitor,
        coroutineScope = coroutineScope,
    )
}

@Stable
class AsAppState(
    errorMonitor: ErrorMonitor,
    val coroutineScope: CoroutineScope,
) : ErrorMonitor by errorMonitor {
    val isOfflineState: StateFlow<Boolean> = isOffline.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false,
    )
    val snackbarMessage: StateFlow<ErrorMessage?> = errorMessage.stateIn(
        scope = coroutineScope,
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
//                Toaster.show(R.string.app_HomeActivity_exit)
                exitTime = currentTimeMillis
            } else {
                context.getActivity().finish()
            }
        } else {
            onBack()
        }
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
