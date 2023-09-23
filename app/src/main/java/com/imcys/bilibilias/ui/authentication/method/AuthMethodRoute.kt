package com.imcys.bilibilias.ui.authentication.method

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ROUTE_AUTH_METHOD = "auth_method"

fun NavController.navigateToAuthMethod() {
    navigate(ROUTE_AUTH_METHOD) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.authMethodRoute(onNavigateToLoginAuth: () -> Unit) =
    composable(ROUTE_AUTH_METHOD) {
        AuthMethodRoute(onNavigateToLoginAuth)
    }

@Composable
fun AuthMethodRoute(onNavigateToLoginAuth: () -> Unit, modifier: Modifier = Modifier) {
    AuthMethodScreen(onNavigateToLoginAuth, modifier)
}
