package com.imcys.bilibilias.feature.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashContent(
    component: SplashComponent,
    navigationToLogin: () -> Unit,
    navigationToHome: () -> Unit,
) {
    if (component.isLogin) {
        navigationToHome()
    } else {
        navigationToLogin()
    }
}
