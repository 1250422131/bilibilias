package com.imcys.authentication.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.authentication.login.LoginAuthRoute

const val ROUTE_AUTH_LOGIN = "auth_login"

fun NavController.navigateToLoginAuth() {
    navigate(ROUTE_AUTH_LOGIN) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.loginAuthRoute(navigateToHome: () -> Unit) = composable(ROUTE_AUTH_LOGIN) {
    LoginAuthRoute(navigateToHome)
}


