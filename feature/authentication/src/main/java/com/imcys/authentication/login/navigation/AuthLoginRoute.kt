package com.imcys.authentication.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.authentication.AuthViewModel
import com.imcys.authentication.login.LoginAuthScreen

const val ROUTE_AUTH_LOGIN = "auth_login"

fun NavController.navigateToLoginAuth() {
    navigate(ROUTE_AUTH_LOGIN) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.loginAuthRoute(navigateToHome: () -> Unit) = composable(ROUTE_AUTH_LOGIN) {
    LoginAuthRoute(navigateToHome)
}

@Composable
fun LoginAuthRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginAuthState by viewModel.loginAuthUiState.collectAsStateWithLifecycle()
    LoginAuthScreen(
        onNavigateToHome = onNavigateToHome,
        onGetQRCode = viewModel::getQRCode,
        onDownloadQRCode = viewModel::downloadQRCode,
        goToBiliBiliQRScan = viewModel::goToBilibiliQRScan,
        loginAuthState = loginAuthState,
        modifier = modifier
    )
}
