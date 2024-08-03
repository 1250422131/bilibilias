package com.imcys.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.home.HomeScreen
import com.imcys.home.HomeViewModel

const val ROUTE_HOME = "home"
fun NavController.navigateToHome() {
    navigate(ROUTE_HOME) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeScreen(
    navigationToDonation: () -> Unit,
    navigateToContribute: () -> Unit
) = composable(ROUTE_HOME) {
    HomeRoute(
        navigationToDonation,
        navigateToContribute
    )
}

@Composable
internal fun HomeRoute(navigationToDonation: () -> Unit, navigateToContribute: () -> Unit) {
    val viewModel: HomeViewModel = hiltViewModel()
    HomeScreen(
        logout = viewModel::logout,
        navigationToDonation,
        navigateToContribute
    )
}
