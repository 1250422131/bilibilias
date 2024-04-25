package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import com.imcys.bilibilias.navigation.TopLevelDestination

@Composable
fun TabIcon(tab: Tab, topLevelDestination: TopLevelDestination): Painter {
    val tabNavigator = LocalTabNavigator.current
    return if (tabNavigator.current.key == tab.key) {
        painterResource(topLevelDestination.selectedIcon)
    } else {
        painterResource(topLevelDestination.unselectedIcon)
    }
}
