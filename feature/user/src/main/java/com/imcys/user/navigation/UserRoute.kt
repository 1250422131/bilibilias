package com.imcys.user.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.user.User
import com.imcys.user.UserViewModel

const val ROUTE_USER = "user"
fun NavController.navigateToUser() {
    navigate(ROUTE_USER) {
        popUpTo(graph.findStartDestination().id)
        launchSingleTop = true
    }
}

fun NavGraphBuilder.userRoute(
    onNavigateTo: () -> Unit,
    onBack: () -> Unit
) = composable(ROUTE_USER) {
    UserRoute()
}

@Composable
fun UserRoute() {
    val viewModel: UserViewModel = hiltViewModel()
    User(viewModel)
}