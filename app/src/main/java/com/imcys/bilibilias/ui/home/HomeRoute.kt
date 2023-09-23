package com.imcys.bilibilias.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.AsBottomBar
import com.imcys.bilibilias.MainScreen

const val ROUTE_HOME = "home"
fun NavController.navigateToHome() {
    navigate(ROUTE_HOME) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeRoute() {
    composable(route = ROUTE_HOME) {
        Scaffold(bottomBar = {
            AsBottomBar()
        }) {
            MainScreen(modifier = Modifier.padding(it))
        }
    }
}

@Composable
fun HomeRoute(
    onNavigateToTool: () -> Unit,
    onNavigateToDownload: () -> Unit,
    onNavigateToUser: () -> Unit
) {
    HomeScreen(
        onNavigateToTool,
        onNavigateToDownload,
        onNavigateToUser
    )
}
