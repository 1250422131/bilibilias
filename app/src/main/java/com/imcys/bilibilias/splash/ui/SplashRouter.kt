package com.imcys.bilibilias.splash.ui

sealed class SplashRouter(val route: String) {
    data object App : SplashRouter("app")
    data object Screen : SplashRouter("home")
}
