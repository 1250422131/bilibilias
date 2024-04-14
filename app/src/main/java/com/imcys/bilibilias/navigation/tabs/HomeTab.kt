package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.feature.home.HomeRoute
import com.imcys.bilibilias.navigation.TopLevelDestination

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val tabNavigator = LocalTabNavigator.current
            val title = stringResource(TopLevelDestination.Home.iconTextId)
            val icon =
                if (tabNavigator.current.key == this.key) painterResource(TopLevelDestination.Home.selectedIcon)
                else painterResource(TopLevelDestination.Home.unselectedIcon)
            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        HomeRoute({}, {})
    }
}