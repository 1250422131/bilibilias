package com.imcys.bilibilias.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.imcys.bilibilias.core.ui.TrackDisposableJank
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberNiaAppState(
    windowSizeClass: WindowSizeClass,
//    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    tabNavigator: TabNavigator = LocalTabNavigator.current
): AsAppState {
    NavigationTrackingSideEffect(tabNavigator)
    return remember(
        navController,
        coroutineScope,
        tabNavigator
    ) {
        AsAppState(
            windowSizeClass = windowSizeClass,
            coroutineScope = coroutineScope,
            tabNavigator = tabNavigator,
        )
    }
}

@Stable
class AsAppState(
    val coroutineScope: CoroutineScope,
    val tabNavigator: TabNavigator,
    val windowSizeClass: WindowSizeClass,
//    networkMonitor: NetworkMonitor,
) {
    fun currentDestination(topLevelDestination: TopLevelDestination): Boolean {
        return tabNavigator.current.key == topLevelDestination.tab.key
    }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            tabNavigator.current = topLevelDestination.tab
        }
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(
    tabNavigator: TabNavigator
) {
    TrackDisposableJank(tabNavigator) { metricsHolder ->
        metricsHolder.state?.putState("Navigation", tabNavigator.current.toString())
        onDispose {
        }
    }
}
