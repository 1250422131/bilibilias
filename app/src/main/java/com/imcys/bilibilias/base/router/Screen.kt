package com.imcys.bilibilias.base.router

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.imcys.bilibilias.R
import com.imcys.bilibilias.ui.download.ROUTE_DOWNLOAD
import com.imcys.bilibilias.ui.home.ROUTE_HOME
import com.imcys.bilibilias.ui.tool.ROUTE_TOOL
import com.imcys.bilibilias.ui.user.ROUTE_USER
@Immutable
sealed class Screen(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    open var navigate: () -> Unit = {}

    data object Home :
        Screen(ROUTE_HOME, R.string.app_home_menu_button_navigation_home, R.drawable.ic_home_home) {
        override var navigate: () -> Unit = {}
    }

    data object Tool :
        Screen(ROUTE_TOOL, R.string.app_home_menu_button_navigation_tool, R.drawable.ic_home_tool) {
        override var navigate: () -> Unit = {}
    }

    data object Download : Screen(
        ROUTE_DOWNLOAD,
        R.string.app_home_menu_button_navigation_download,
        R.drawable.ic_home_download_four
    ) {
        override var navigate: () -> Unit = {}
    }

    data object User :
        Screen(ROUTE_USER, R.string.app_home_menu_button_navigation_user, R.drawable.ic_home_people) {
        override var navigate: () -> Unit = {}
    }
}
