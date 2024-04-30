package com.imcys.bilibilias.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.ui.TrackDisposableJank
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberNiaAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AsAppState {
    return remember(
        coroutineScope,
    ) {
        AsAppState(
            networkMonitor=networkMonitor,
            windowSizeClass = windowSizeClass,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class AsAppState(
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    fun currentDestination(tabNavigator: TabNavigator, topLevelDestination: TopLevelDestination): Boolean {
        return tabNavigator.current.key == topLevelDestination.tab.key
    }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries
    val topLevelTabs: List<Tab> = TopLevelDestination.entries.map { it.tab }

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(tabNavigator: TabNavigator, topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            tabNavigator.current = topLevelDestination.tab
        }
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
internal fun NavigationTrackingSideEffect(tabNavigator: TabNavigator) {
    TrackDisposableJank(tabNavigator) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", tabNavigator.current.toString())
        onDispose {}
    }
}
