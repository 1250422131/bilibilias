package com.imcys.bilibilias.ui.tool

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ROUTE_TOOL = "tool"
fun NavController.navigateToTool() {
    navigate(ROUTE_TOOL) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.toolRoute(
    onNavigateToPlayer: () -> Unit,
    onBack: () -> Unit
) = composable(ROUTE_TOOL) {
    ToolRoute(onNavigateToPlayer, onBack)
}

@Composable
fun ToolRoute(onNavigateToPlayer: () -> Unit, onBack: () -> Unit) {
    val viewModel: ToolViewModel = hiltViewModel()
    val state by viewModel.toolState.collectAsStateWithLifecycle()
    // 清空搜索框文本 clearSearchText
    // viewModel::clearSearchText
    ToolScreen(
        state,
        viewModel::parsesBvOrAvOrEp,
        viewModel::clearSearchText,
        onNavigateToPlayer,
        onBack,
    )
}
