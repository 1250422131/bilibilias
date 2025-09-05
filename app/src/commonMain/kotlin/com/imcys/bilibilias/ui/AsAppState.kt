package com.imcys.bilibilias.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.imcys.bilibilias.core.data.model.MessageData
import com.imcys.bilibilias.core.data.util.ErrorMonitor
import com.imcys.bilibilias.core.navigation.AsBackStack
import com.imcys.bilibilias.navigation.TopLevelDestination
import com.imcys.bilibilias.navigation.TopLevelDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberAsAppState(
    errorMonitor: ErrorMonitor,
    asBackStack: AsBackStack,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AsAppState {
    return remember(
        coroutineScope,
        errorMonitor,
        asBackStack,
    ) {
        AsAppState(
            coroutineScope = coroutineScope,
            errorMonitor = errorMonitor,
            asBackStack = asBackStack,
        )
    }
}

@androidx.compose.runtime.Stable
class AsAppState(
    coroutineScope: CoroutineScope,
    val errorMonitor: ErrorMonitor,
    val asBackStack: AsBackStack,
) {
    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = TopLevelDestinations[asBackStack.currentTopLevelKey]
    val stateMessage: StateFlow<MessageData?> = errorMonitor.messages.map {
        it.firstOrNull()
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )
}