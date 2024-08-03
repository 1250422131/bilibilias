package com.imcys.bilibilias.ui.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.ui.splash.SplashRoute

const val ROUTE_SPLASH = "splash"

fun NavGraphBuilder.splashRoute(
    navigateToAuthMethod: () -> Unit,
    navigateToHome: () -> Unit,
) = composable(ROUTE_SPLASH) {
    SplashRoute(navigateToAuthMethod, navigateToHome)
}