package com.imcys.bilibilias.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.home.navigation.homeScreen
import com.imcys.bilibilias.ui.home.navigation.navigateToHome
import com.imcys.bilibilias.ui.login.navigation.loginScreen
import com.imcys.bilibilias.ui.login.navigation.navigateToLogin
import com.imcys.bilibilias.ui.login.navigation.navigateToQRCodeLogin
import com.imcys.bilibilias.ui.login.navigation.qrCodeLoginScreen


/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BILIBILIASNavHost(
    navController: NavHostController,
) {
    SharedTransitionLayout {
        NavHost(
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
            navController = navController,
            startDestination = HomeRoute(),
            // 正向导航：新页面进入 - 只是淡入
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            // 正向导航：原页面退出 - 放大并保持可见
            exitTransition = {
                scaleOut(
                    targetScale = 1.1F, // 放大到 1.1 倍
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            // 返回导航：上一个页面进入 - 从放大状态恢复
            popEnterTransition = {
                scaleIn(
                    initialScale = 1.1F, // 从 1.1 倍开始
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            },
            // 返回导航：当前页面退出 - 淡出
            popExitTransition = {
                scaleOut(
                    targetScale = 1.1F, // 放大到 1.1 倍
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            },
        ) {
            homeScreen(goToLogin = navController::navigateToLogin)

            loginScreen(
                onToBack = navController::popBackStack,
                goToQRCodeLogin = navController::navigateToQRCodeLogin
            )

            qrCodeLoginScreen(
                onToBack = navController::popBackStack,
                onBackHomePage = {
                    navController.navigateToHome(
                        homeRoute = HomeRoute(isFormLogin = true),
                    ){
                        popUpTo<HomeRoute>()
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}


