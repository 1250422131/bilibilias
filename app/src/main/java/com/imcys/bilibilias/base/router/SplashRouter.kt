package com.imcys.bilibilias.base.router

sealed class SplashRouter(val route: String) {
    data object App : SplashRouter("app")
    data object Screen : SplashRouter("home")
}
