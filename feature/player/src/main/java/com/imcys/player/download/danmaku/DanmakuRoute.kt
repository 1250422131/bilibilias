package com.imcys.player.download.danmaku

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val ROUTE_DANMAKU = "danmaku"
fun NavController.navigateToDanmaku() {
    navigate(ROUTE_DANMAKU)
}

fun NavGraphBuilder.danmakuRoute(
    navController: NavHostController
) = composable(ROUTE_DANMAKU) { backStackEntry ->
}

