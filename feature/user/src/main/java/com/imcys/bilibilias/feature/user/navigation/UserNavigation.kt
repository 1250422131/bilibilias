package com.imcys.bilibilias.feature.user.navigation

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.feature.user.UserRoute
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationToUser() {
    UserRoute()
}