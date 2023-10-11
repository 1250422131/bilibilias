package com.imcys.bilibilias.ui.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ROUTE_SETTINGS = "settings"
fun NavController.navigateToSettings() {
    navigate(ROUTE_SETTINGS)
}

fun NavGraphBuilder.settingsRoute() = composable(ROUTE_SETTINGS) {
    SettingsRoute()
}

@Composable
fun SettingsRoute() {
    SettingsScreen()
}
