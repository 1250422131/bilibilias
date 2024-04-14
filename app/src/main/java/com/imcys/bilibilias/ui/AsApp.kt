package com.imcys.bilibilias.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.component.AsGradientBackground
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBar
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBarItem
import com.imcys.bilibilias.navigation.TopLevelDestination
import com.imcys.bilibilias.navigation.tabs.DownloadTab
import com.imcys.bilibilias.navigation.tabs.HomeTab
import com.imcys.bilibilias.navigation.tabs.ToolTab
import com.imcys.bilibilias.navigation.tabs.UserTab

@Composable
fun AsApp(appState: AsAppState) {
    AsBackground {
        AsGradientBackground {
            TabNavigator(
                HomeTab,
                tabDisposable = {
                    TabDisposable(
                        navigator = it,
                        tabs = listOf(HomeTab, ToolTab, DownloadTab, UserTab)
                    )
                }
            ) { tabNavigator ->
                Scaffold(
                    bottomBar = {
                        AsBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState::currentDestination,
                            modifier = Modifier.testTag("AsBottomBar")
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CurrentTab()
                    }
                }
            }
        }
    }
}

@Composable
private fun AsBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TabNavigator, TopLevelDestination) -> Unit,
    currentDestination: (TabNavigator, TopLevelDestination) -> Boolean,
    modifier: Modifier = Modifier,
) {
    AsNavigationBar(
        modifier = modifier,
    ) {
        val tabNavigator = LocalTabNavigator.current
        destinations.forEach { destination ->
            val selected = currentDestination(tabNavigator, destination)
            AsNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(tabNavigator, destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.unselectedIcon),
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        painter = painterResource(id = destination.selectedIcon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = {
                    Text(
                        stringResource(destination.iconTextId),
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified
                    )
                },
                alwaysShowLabel = selected,
                modifier = Modifier,
            )
        }
    }
}
