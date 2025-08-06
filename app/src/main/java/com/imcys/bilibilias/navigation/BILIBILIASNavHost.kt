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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.imcys.bilibilias.ui.analysis.navigation.analysisScreen
import com.imcys.bilibilias.ui.analysis.navigation.navigateToAnalysis
import com.imcys.bilibilias.ui.download.navigation.downloadScreen
import com.imcys.bilibilias.ui.download.navigation.navigateToDownload
import com.imcys.bilibilias.ui.home.navigation.HomeRoute
import com.imcys.bilibilias.ui.home.navigation.homeScreen
import com.imcys.bilibilias.ui.home.navigation.navigateToHome
import com.imcys.bilibilias.ui.login.navigation.loginScreen
import com.imcys.bilibilias.ui.login.navigation.navigateToLogin
import com.imcys.bilibilias.ui.login.navigation.navigateToQRCodeLogin
import com.imcys.bilibilias.ui.login.navigation.qrCodeLoginScreen
import com.imcys.bilibilias.ui.setting.navigation.navigateToRoam
import com.imcys.bilibilias.ui.setting.navigation.navigateToSetting
import com.imcys.bilibilias.ui.setting.navigation.roamScreen
import com.imcys.bilibilias.ui.setting.navigation.settingScreen
import com.imcys.bilibilias.ui.user.navigation.UserRoute
import com.imcys.bilibilias.ui.user.navigation.navigateToUser
import com.imcys.bilibilias.ui.user.navigation.userScreen


/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Deprecated(message = "改用Nav3",replaceWith = ReplaceWith("BILIBILAISNavDisplay()"))
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

            homeScreen(
                this@SharedTransitionLayout,
                goToLogin = navController::navigateToLogin,
                goToUserPage = {
                    navController.navigateToUser(
                        userRoute = UserRoute(mid = it),
                    ) {
                        popUpTo<HomeRoute>()
                        launchSingleTop = true
                    }
                },
                goToAnalysis = navController::navigateToAnalysis,
                goToDownloadPage = navController::navigateToDownload
            )

            loginScreen(
                onToBack = navController::popToRootAtMost,
                goToQRCodeLogin = navController::navigateToQRCodeLogin
            )

            qrCodeLoginScreen(
                onToBack = navController::popToRootAtMost,
                onBackHomePage = {
                    navController.navigateToHome(
                        homeRoute = HomeRoute(isFormLogin = true),
                    ) {
                        popUpTo<HomeRoute>()
                        launchSingleTop = true
                    }
                }
            )

            userScreen(
                onToBack = navController::popToRootAtMost,
                onToSettings = navController::navigateToSetting
            )

            analysisScreen(
                this@SharedTransitionLayout,
                onToBack = navController::popToRootAtMost,
                goToUser = {
                    navController.navigateToUser(
                        UserRoute(
                            mid = it,
                            isAnalysisUser = true
                        )
                    )
                }
            )

            downloadScreen(onToBack = navController::popToRootAtMost)

            settingScreen(
                onToRoam = navController::navigateToRoam,
                onToBack = navController::popToRootAtMost
            )

            roamScreen(onToBack = navController::popToRootAtMost)
        }
    }
}

/**
 * 防止在没有上一个页面时调用 popBackStack 导致崩溃
 * 参考：https://github.com/google/accompanist/issues/1396#issuecomment-1432694270
 */
fun NavController.popToRootAtMost() {
    if (previousBackStackEntry == null) return
    popBackStack()
}
