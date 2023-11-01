package com.imcys.bilibilias.tool.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.tool.ToolRoute
import com.imcys.bilibilias.tool.ToolScreen
import com.imcys.bilibilias.tool.ToolViewModel

const val ROUTE_TOOL = "tool"
fun NavController.navigateToTool() {
    navigate(ROUTE_TOOL) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.toolScreen(
    navigateToPlayer: () -> Unit,
    navigateToSetting: () -> Unit,
    navigateToExportBangumiFollowList: () -> Unit,
) = composable(ROUTE_TOOL) {
    ToolRoute(navigateToPlayer, navigateToSetting, navigateToExportBangumiFollowList)
}

