package com.imcys.setting.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.setting.SettingsScreen

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
