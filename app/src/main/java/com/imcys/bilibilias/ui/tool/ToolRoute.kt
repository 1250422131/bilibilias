package com.imcys.bilibilias.ui.tool

import androidx.compose.runtime.Composable
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
    onNavigateTo: () -> Unit,
    onBack: () -> Unit
) = composable(ROUTE_TOOL) {
    ToolRoute()
}

@Composable
fun ToolRoute() {
    Tool()
}