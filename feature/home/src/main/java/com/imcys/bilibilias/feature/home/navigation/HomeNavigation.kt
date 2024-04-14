package com.imcys.bilibilias.feature.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.imcys.bilibilias.feature.home.HomeRoute

@Composable
fun HomeFragmentScreen(onSalute: () -> Unit, onDonation: () -> Unit) {
    HomeRoute(onSalute, onDonation)
}
