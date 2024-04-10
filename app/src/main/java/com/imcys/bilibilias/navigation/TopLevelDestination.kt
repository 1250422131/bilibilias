package com.imcys.bilibilias.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.imcys.bilibilias.R
import com.imcys.bilibilias.feature.download.navigation.destinations.NavigationToDownloadDestination
import com.imcys.bilibilias.feature.home.navigation.destinations.NavigationToHomeDestination
import com.imcys.bilibilias.feature.tool.navigation.destinations.NavigationToToolDestination
import com.imcys.bilibilias.feature.user.navigation.destinations.NavigationToUserDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class TopLevelDestination(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
    @StringRes val iconTextId: Int,
    ) {
        Home(
            direction = NavigationToHomeDestination,
            icon = R.drawable.ic_home_home_all,
            iconTextId = R.string.app_home_menu_button_navigation_home
        ),
        Tool(
            direction = NavigationToToolDestination,
            icon = R.drawable.ic_home_tool_all,
            iconTextId = R.string.app_home_menu_button_navigation_tool
        ),
        Download(
            direction = NavigationToDownloadDestination,
            icon = R.drawable.ic_home_download_four_all,
            iconTextId = R.string.app_home_menu_button_navigation_download
        ),
        User(
            direction = NavigationToUserDestination,
            icon = R.drawable.ic_home_people_all,
            iconTextId = R.string.app_home_menu_button_navigation_user
        )
    }