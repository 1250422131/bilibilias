package com.imcys.bilibilias.navigation.tabs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.imcys.bilibilias.navigation.TopLevelDestination

abstract class ScreenX : Screen {

    @Composable
    abstract fun Content(modifier: Modifier)

    @Composable
    final override fun Content() = Content(Modifier)
}

abstract class TabX : ScreenX(), Tab {

    public abstract override val options: TabOptions
        @Composable get
}

@Composable
public fun CurrentTab(modifier: Modifier) {
    val tabNavigator = LocalTabNavigator.current
    val currentTab = tabNavigator.current
    require(currentTab is TabX) {
        "请继承 TabX \n https://github.com/adrielcafe/voyager/issues/26"
    }
    tabNavigator.saveableState("currentTab") {
        currentTab.Content(modifier)
    }
}

@Composable
fun TabIcon(tab: Tab, topLevelDestination: TopLevelDestination): Painter {
    val tabNavigator = LocalTabNavigator.current
    return if (tabNavigator.current.key == tab.key) {
        painterResource(topLevelDestination.selectedIcon)
    } else {
        painterResource(topLevelDestination.unselectedIcon)
    }
}
