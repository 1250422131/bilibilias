package com.imcys.bilibilias.base.router

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.imcys.bilibilias.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    data object Home :
        Screen("home", R.string.app_home_menu_button_navigation_home, R.drawable.ic_home_home)

    data object Tool :
        Screen("tool", R.string.app_home_menu_button_navigation_tool, R.drawable.ic_home_tool)

    data object Download : Screen(
        "download",
        R.string.app_home_menu_button_navigation_download,
        R.drawable.ic_home_download_four
    )

    data object User :
        Screen("user", R.string.app_home_menu_button_navigation_user, R.drawable.ic_home_people)
}

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavController found!") }
