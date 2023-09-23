package com.imcys.bilibilias.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.home.ui.viewmodel.HomeViewModel

const val ROUTE_HOME = "home"
fun NavController.navigateToHome() {
    navigate(ROUTE_HOME) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeRoute() = composable(ROUTE_HOME) {
    HomeRoute()
}

@Composable
fun HomeRoute() {
    val viewModel: HomeViewModel = hiltViewModel()
    HomeScreen(
        goToNewVersionDoc = viewModel::goToNewVersionDoc,
        goToCommunity = viewModel::goToCommunity,
        goToDonateList = viewModel::goToDonateList,
        logoutLogin = viewModel::logoutLogin
    )
}
