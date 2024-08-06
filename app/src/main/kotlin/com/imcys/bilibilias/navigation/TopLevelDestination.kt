package com.imcys.bilibilias.navigation

import com.imcys.bilibilias.R

enum class TopLevelDestination(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIconId = R.drawable.ic_home_home_true,
        unselectedIconId = R.drawable.ic_home_home,
        iconTextId = R.string.app_home_menu_button_navigation_home,
        titleTextId = R.string.app_home_menu_button_navigation_home,
    ),
    TOOL(
        selectedIconId = R.drawable.ic_home_tool_true,
        unselectedIconId = R.drawable.ic_home_tool,
        iconTextId = R.string.app_home_menu_button_navigation_tool,
        titleTextId = R.string.app_home_menu_button_navigation_tool,
    ),
    DOWNLOAD(
        selectedIconId = R.drawable.ic_home_download_four_true,
        unselectedIconId = R.drawable.ic_home_download_four,
        iconTextId = R.string.app_home_menu_button_navigation_download,
        titleTextId = R.string.app_home_menu_button_navigation_download,
    ),
    USER(
        selectedIconId = R.drawable.ic_home_people_true,
        unselectedIconId = R.drawable.ic_home_people,
        iconTextId = R.string.app_home_menu_button_navigation_user,
        titleTextId = R.string.app_home_menu_button_navigation_user,
    ),
}
