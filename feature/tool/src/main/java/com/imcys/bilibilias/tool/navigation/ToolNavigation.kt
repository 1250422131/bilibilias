package com.imcys.bilibilias.tool.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.tool.ToolRoute

const val ROUTE_TOOL = "tool"
fun NavController.navigateToTool() {
    navigate(ROUTE_TOOL) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.toolScreen(
    navigateToPlayer: (Long, String, Long,)-> Unit,
    navigateToSetting: () -> Unit,
    navigateToExportBangumiFollowList: () -> Unit,
) = composable(ROUTE_TOOL) {
    ToolRoute(navigateToPlayer, navigateToSetting, navigateToExportBangumiFollowList)
}

