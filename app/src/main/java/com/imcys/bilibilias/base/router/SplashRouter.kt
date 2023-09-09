package com.imcys.bilibilias.base.router

sealed class SplashRouter(val route: String) {
    data object App : SplashRouter(RouterConstants.App)
    data object Screen : SplashRouter(RouterConstants.Home)
    data object AuthMethod : SplashRouter(RouterConstants.Auth1)
    data object AuthScreen : SplashRouter(RouterConstants.Auth2)
}
