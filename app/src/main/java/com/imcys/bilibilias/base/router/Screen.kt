package com.imcys.bilibilias.base.router

import androidx.annotation.StringRes
import com.imcys.bilibilias.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object Home : Screen("home", R.string.app_home_menu_button_navigation_home)
    data object Tool : Screen("tool", R.string.app_home_menu_button_navigation_tool)
    data object Download : Screen("download", R.string.app_home_menu_button_navigation_download)
    data object User : Screen("user", R.string.app_home_menu_button_navigation_user)
}
