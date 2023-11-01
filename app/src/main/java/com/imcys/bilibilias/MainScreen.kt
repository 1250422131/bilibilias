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
import com.imcys.bilibilias.tool.bangumiFollow.bangumiFollowRoute
import com.imcys.bilibilias.tool.bangumiFollow.navigateToBangumiFollow
import com.imcys.bilibilias.tool.navigation.toolRoute
import com.imcys.bilibilias.ui.download.danmaku.danmakuRoute
import com.imcys.bilibilias.ui.download.danmaku.navigateToDanmaku
import com.imcys.bilibilias.ui.download.downloadOptionsRoute
import com.imcys.bilibilias.ui.download.downloadRoute
import com.imcys.bilibilias.ui.download.navigateToDownloadOptions
import com.imcys.bilibilias.ui.home.ROUTE_HOME
import com.imcys.bilibilias.ui.home.homeRoute
import com.imcys.bilibilias.ui.play.navigateToPlayer
import com.imcys.bilibilias.ui.play.playerRoute
import com.imcys.setting.navigation.navigateToSettings
import com.imcys.setting.navigation.settingsRoute
import com.imcys.user.navigation.userRoute

const val ROUTE_MAIN_SCREEN = "main_screen"

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    BILIBILIASAnimatedNavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ROUTE_HOME,
        route = ROUTE_MAIN_SCREEN,
    ) {
        homeRoute()
        toolRoute(
            onNavigateToPlayer = navController::navigateToPlayer,
            onNavigateToSettings = navController::navigateToSettings,
            onNavigateToBangumiFollow = navController::navigateToBangumiFollow
        )
        bangumiFollowRoute()
        settingsRoute()
        downloadRoute(onNavigateTo = {}, onBack = navController::navigateUp)
        // <editor-fold desc="下载选项">
        downloadOptionsRoute(
            onBack = navController::navigateUp,
            navController = navController,
        )
        // </editor-fold>

        userRoute(onNavigateTo = {}, onBack = navController::navigateUp)

        playerRoute(
            onNavigateToDownloadOption = navController::navigateToDownloadOptions,
            navController = navController,
            onNavigateToDownloadAanmaku = navController::navigateToDanmaku,
        )
        danmakuRoute(navController = navController, onBack = navController::navigateUp)
    }
}

@Composable
fun BILIBILIASAnimatedNavHost(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String,
    route: String,
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
        route = route,
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
