package com.imcys.bilibilias.feature.home.navigation

import androidx.compose.runtime.Composable
import com.imcys.bilibilias.feature.home.HomeRoute
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationToHome() {
    HomeRoute({}, {})
}

@Composable
fun HomeFragmentScreen(onSalute: () -> Unit, onDonation: () -> Unit) {
    HomeRoute(onSalute, onDonation)
}
