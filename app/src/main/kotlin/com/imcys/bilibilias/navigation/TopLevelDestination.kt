package com.imcys.bilibilias.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cafe.adriel.voyager.navigator.tab.Tab
import com.imcys.bilibilias.R
import com.imcys.bilibilias.navigation.tabs.DownloadTab
import com.imcys.bilibilias.navigation.tabs.HomeTab
import com.imcys.bilibilias.navigation.tabs.ToolTab
import com.imcys.bilibilias.navigation.tabs.UserTab

enum class TopLevelDestination(
    val tab: Tab,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val iconTextId: Int,
) {
    Home(
        HomeTab,
        selectedIcon = R.drawable.ic_home_home_true,
        unselectedIcon = R.drawable.ic_home_home,
        iconTextId = R.string.app_home_menu_button_navigation_home
    ),
    Tool(
        ToolTab,
        selectedIcon = R.drawable.ic_home_tool_true,
        unselectedIcon = R.drawable.ic_home_tool,
        iconTextId = R.string.app_home_menu_button_navigation_tool
    ),
    Download(
        DownloadTab,
        selectedIcon = R.drawable.ic_home_download_four_true,
        unselectedIcon = R.drawable.ic_home_download_four,
        iconTextId = R.string.app_home_menu_button_navigation_download
    ),
    User(
        UserTab,
        selectedIcon = R.drawable.ic_home_people_true,
        unselectedIcon = R.drawable.ic_home_people,
        iconTextId = R.string.app_home_menu_button_navigation_user
    )
}
