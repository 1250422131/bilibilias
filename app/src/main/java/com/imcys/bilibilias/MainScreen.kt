package com.imcys.bilibilias

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.imcys.bilibilias.ui.download.downloadRoute
import com.imcys.bilibilias.ui.home.ROUTE_HOME
import com.imcys.bilibilias.ui.home.homeRoute
import com.imcys.bilibilias.ui.player.navigateToPlayer
import com.imcys.bilibilias.ui.player.playerRoute
import com.imcys.bilibilias.ui.tool.toolRoute
import com.imcys.bilibilias.ui.user.userRoute

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    BILIBILIASAnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ROUTE_HOME,
    ) {
        homeRoute()
        toolRoute(onNavigateToPlayer = navController::navigateToPlayer, onBack = navController::navigateUp)
        downloadRoute(onNavigateTo = {}, onBack = navController::navigateUp)
        userRoute(onNavigateTo = {}, onBack = navController::navigateUp)

        playerRoute(onBack = navController::navigateUp)
    }
}

@Composable
fun BILIBILIASAnimatedNavHost(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            activityEnterTransition()
        },
        exitTransition = {
            activityExitTransition()
        },
        popEnterTransition = {
            activityPopEnterTransition()
        },
        popExitTransition = {
            activityPopExitTransition()
        },
        builder = builder
    )
}

// region 动画配置

private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityEnterTransition(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
        animationSpec = tween(DEFAULT_ENTER_DURATION, easing = LinearOutSlowInEasing),
        initialOffset = { it }
    )
}

@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityExitTransition(): ExitTransition {
    return scaleOut(
        animationSpec = tween(DEFAULT_ENTER_DURATION),
        targetScale = 0.96F
    )
}

@Suppress("UnusedReceiverParameter")
private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopEnterTransition(): EnterTransition {
    return scaleIn(
        animationSpec = tween(DEFAULT_EXIT_DURATION),
        initialScale = 0.96F
    )
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.activityPopExitTransition(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.End,
        animationSpec = tween(DEFAULT_EXIT_DURATION, easing = FastOutLinearInEasing),
        targetOffset = { it }
    )
}
// endregion

private const val DEFAULT_ENTER_DURATION = 300
private const val DEFAULT_EXIT_DURATION = 220
