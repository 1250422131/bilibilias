package com.imcys.bilibilias.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.tracing.trace
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberNiaAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): AsAppState {
//    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
    ) {
        AsAppState(
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class AsAppState(
    val coroutineScope: CoroutineScope,
    val navController: NavHostController,
) {
    fun currentDestination(tabNavigator: TabNavigator, topLevelDestination: TopLevelDestination): Boolean {
        return tabNavigator.current.key == topLevelDestination.tab.key
    }
//    val shouldShowBottomBar: Boolean
//        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(
        tabNavigator: TabNavigator,
        topLevelDestination: TopLevelDestination
    ) {
        trace("Navigation: ${topLevelDestination.name}") {
            tabNavigator.current = topLevelDestination.tab
        }
    }
}
