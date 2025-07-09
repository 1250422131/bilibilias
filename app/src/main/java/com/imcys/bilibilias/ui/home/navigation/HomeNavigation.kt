package com.imcys.bilibilias.ui.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.imcys.bilibilias.ui.home.HomeRoute
import kotlinx.serialization.Serializable

const val HOME_PATH = "home"


@Serializable
data class HomeRoute(
    var isFormLogin: Boolean = false
)


fun NavController.navigateToHome(
    homeRoute: HomeRoute = HomeRoute(),
    builder: (NavOptionsBuilder.() -> Unit)? = null
) = navigate(homeRoute, builder ?: {})


@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeScreen(
    sharedTransitionScope: SharedTransitionScope,
    goToLogin: () -> Unit,
    goToUserPage: (mid: Long) -> Unit,
    goToAnalysis: () -> Unit,
    ) {
    composable<HomeRoute> { navBackStackEntry ->
        // 从 navBackStackEntry 获取参数
        val homeRoute = navBackStackEntry.toRoute<HomeRoute>()

        HomeRoute(
            homeRoute,
            sharedTransitionScope,
            this@composable,
            goToLogin,
            goToUserPage,
            goToAnalysis
        )
    }
}