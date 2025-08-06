package com.imcys.bilibilias.ui.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation3.runtime.NavKey
import com.imcys.bilibilias.ui.setting.SettingScreen
import com.imcys.bilibilias.ui.setting.roam.RoamScreen
import kotlinx.serialization.Serializable

@Serializable
object SettingRoute: NavKey

@Serializable
object RoamRoute: NavKey

fun NavController.navigateToSetting(navOptions: NavOptions? = null) =
    navigate(SettingRoute, navOptions)

fun NavController.navigateToRoam(navOptions: NavOptions? = null) =
    navigate(RoamRoute, navOptions)

fun NavGraphBuilder.settingScreen(
    onToRoam: () -> Unit,
    onToBack: () -> Unit,
) {
    composable<SettingRoute> {
        SettingScreen(onToRoam, onToBack)
    }
}

fun NavGraphBuilder.roamScreen(
    onToBack: () -> Unit,
) {
    composable<RoamRoute> {
        RoamScreen(onToBack)
    }
}