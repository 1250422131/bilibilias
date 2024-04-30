package com.imcys.bilibilias.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.imcys.bilibilias.core.common.utils.getActivity
import com.imcys.bilibilias.core.data.util.NetworkMonitor
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.component.AsGradientBackground
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBar
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBarItem
import com.imcys.bilibilias.core.designsystem.theme.AsTheme
import com.imcys.bilibilias.navigation.TopLevelDestination
import com.imcys.bilibilias.navigation.tabs.HomeTab

class MainScreen(private val networkMonitor: NetworkMonitor) : Screen {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Content() {
        val activity = LocalContext.current.getActivity()
        val appState = rememberNiaAppState(
            networkMonitor = networkMonitor,
            windowSizeClass = calculateWindowSizeClass(activity)
        )
        AsTheme {
            AsApp(appState)
        }
    }
}

@Composable
fun AsApp(appState: AsAppState, modifier: Modifier = Modifier) {
    AsBackground {
        AsGradientBackground {
            val snackbarHostState = remember { SnackbarHostState() }

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = "⚠\uFE0F 您没有连接到互联网",
                        duration = SnackbarDuration.Indefinite,
                    )
                }
            }
            AsApp(appState = appState, modifier = modifier, snackbarHostState = snackbarHostState)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AsApp(
    appState: AsAppState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    TabNavigator(
        HomeTab,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = appState.topLevelTabs
            )
        }
    ) {
        NavigationTrackingSideEffect(tabNavigator = it)
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                AsBottomBar(
                    destinations = appState.topLevelDestinations,
                    onNavigateToDestination = appState::navigateToTopLevelDestination,
                    currentDestination = appState::currentDestination,
                    modifier = Modifier.testTag("AsBottomBar"),
                    tabNavigator = it
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                CurrentTab()
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
    tabNavigator: TabNavigator,
) {
    AsNavigationBar(
        modifier = modifier,
    ) {
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
