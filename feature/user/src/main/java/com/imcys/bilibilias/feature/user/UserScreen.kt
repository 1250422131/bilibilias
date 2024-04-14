package com.imcys.bilibilias.feature.user

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class UserScreen : Screen {
    @Composable
    override fun Content() {
        UserRoute()
    }
}

@Composable
internal fun UserRoute() {

}

@Composable
internal fun UserScreen(todo: () -> Unit) {

}