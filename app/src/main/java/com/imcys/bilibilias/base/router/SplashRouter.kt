package com.imcys.bilibilias.base.router

sealed class SplashRouter(val route: String) {
    data object App : SplashRouter("app")
    data object Screen : SplashRouter("home")
    data object AuthMethod : SplashRouter("auth1")
    data object AuthScreen : SplashRouter("auth2")
}
