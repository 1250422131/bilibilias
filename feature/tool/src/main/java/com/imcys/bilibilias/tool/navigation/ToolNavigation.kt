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
    navigateToPlayer: (String, String, String) -> Unit,
    navigateToSetting: () -> Unit,
    navigateToExportBangumiFollowList: () -> Unit,
    navigationToMerge: () -> Unit,
) = composable(ROUTE_TOOL) {
    ToolRoute(
        onNavigateToPlayer = navigateToPlayer,
        onNavigateToSettings = navigateToSetting,
        onNavigateToBangumiFollow = navigateToExportBangumiFollowList,
        navigationToMerge = navigationToMerge
    )
}
