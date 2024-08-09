package com.imcys.bilibilias.feature.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashContent(
    component: SplashComponent,
    navigationToLogin: () -> Unit,
    navigationToTool: () -> Unit,
) {
    if (component.isLogin) {
        navigationToTool()
    } else {
        navigationToLogin()
    }
}
