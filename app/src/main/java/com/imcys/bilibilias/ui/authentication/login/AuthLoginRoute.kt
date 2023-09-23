package com.imcys.bilibilias.ui.authentication.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.ui.authentication.LoginAuthViewModel

const val ROUTE_AUTH_LOGIN = "auth_login"

fun NavController.navigateToLoginAuth() {
    navigate(ROUTE_AUTH_LOGIN) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.loginAuthRoute(onNavigateToHome: () -> Unit) = composable(ROUTE_AUTH_LOGIN) {
    LoginAuthRoute(onNavigateToHome)
}

@Composable
fun LoginAuthRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginAuthViewModel = hiltViewModel()
) {
    val loginAuthState by viewModel.loginAuthUiState.collectAsStateWithLifecycle()
    LoginAuthScreen(
        onNavigateToHome = onNavigateToHome,
        onGetQRCode = viewModel::getQRCode,
        onDownloadQRCode = viewModel::downloadQRCode,
        onGoToBiliBiliQRScan = viewModel::goToBilibiliQRScan,
        loginAuthState = loginAuthState,
        modifier = modifier
    )
}
