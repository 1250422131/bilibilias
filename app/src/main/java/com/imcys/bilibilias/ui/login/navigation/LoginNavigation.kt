package com.imcys.bilibilias.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.ui.login.LoginRoute
import com.imcys.bilibilias.ui.login.QRCodeLoginRoute
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute: NavKey

@Serializable
object QRCodeLoginRoute: NavKey


fun NavController.navigateToLogin(navOptions: NavOptions? = null) =
    navigate(LoginRoute, navOptions)

fun NavController.navigateToQRCodeLogin(navOptions: NavOptions? = null) =
    navigate(QRCodeLoginRoute, navOptions)

fun NavGraphBuilder.loginScreen(
    onToBack: () -> Unit,
    goToQRCodeLogin: () -> Unit
) {
    composable<LoginRoute> {
        LoginRoute(onToBack, goToQRCodeLogin)
    }
}

fun NavGraphBuilder.qrCodeLoginScreen(
    onToBack: () -> Unit,
    onBackHomePage: () -> Unit,
) {
    composable<QRCodeLoginRoute> {
        QRCodeLoginRoute(onToBack, onBackHomePage)
    }
}