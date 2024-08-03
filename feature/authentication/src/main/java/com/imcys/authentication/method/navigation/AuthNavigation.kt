package com.imcys.authentication.method.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.authentication.method.AuthMethodRoute

const val ROUTE_AUTH_METHOD = "auth_method"

fun NavController.navigateToAuthMethod() {
    navigate(ROUTE_AUTH_METHOD) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.authMethodRoute(navigateToLoginAuth: () -> Unit) =
    composable(ROUTE_AUTH_METHOD) {
        AuthMethodRoute(navigateToLoginAuth)
    }
