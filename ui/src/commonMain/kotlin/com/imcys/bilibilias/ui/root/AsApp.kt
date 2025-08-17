package com.imcys.bilibilias.ui.root

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.imcys.bilibilias.logic.root.RootComponent
import com.imcys.bilibilias.ui.cache.CacheScreen
import com.imcys.bilibilias.ui.component.AsBackground
import com.imcys.bilibilias.ui.component.AsGradientBackground
import com.imcys.bilibilias.ui.component.AsNavigationSuiteScaffold
import com.imcys.bilibilias.ui.login.LoginScreen
import com.imcys.bilibilias.ui.navigation.TopLevelDestination
import com.imcys.bilibilias.ui.player.PlayerScreen
import com.imcys.bilibilias.ui.search.SearchScreen
import com.imcys.bilibilias.ui.setting.SettingsScreen
import com.imcys.bilibilias.ui.theme.AsTheme
import com.imcys.bilibilias.ui.theme.GradientColors
import com.imcys.bilibilias.ui.theme.LocalGradientColors

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AsApp(
    component: RootComponent,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance

    AsTheme(false) {
        AsBackground {
            AsGradientBackground {
                AsNavigationSuiteScaffold(
                    navigationSuiteItems = {
                        item(
                            selected = activeComponent is RootComponent.Child.SearchChild,
                            onClick = component::onSearchClicked,
                            icon = {
                                Icon(
                                    imageVector = TopLevelDestination.SEARCH.unselectedIcon,
                                    contentDescription = null,
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    imageVector = TopLevelDestination.SEARCH.selectedIcon,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(TopLevelDestination.SEARCH.iconText) },
                            modifier = Modifier
                                .testTag("NavItem")
                        )
                        item(
                            selected = activeComponent is RootComponent.Child.CacheChild,
                            onClick = component::onCacheClicked,
                            icon = {
                                Icon(
                                    imageVector = TopLevelDestination.CACHE.unselectedIcon,
                                    contentDescription = null,
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    imageVector = TopLevelDestination.CACHE.selectedIcon,
                                    contentDescription = null,
                                )
                            },
                            label = { Text(TopLevelDestination.CACHE.iconText) },
                            modifier = Modifier
                                .testTag("NavItem")
                        )
                    },
                    shouldShowBottomBar = activeComponent.shouldDisplayBottomBar,
                    windowAdaptiveInfo = windowAdaptiveInfo,
                    modifier = modifier
                ) {
                    Children(component)
                }
            }
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(slide()),
            onBack = component::onBackClicked,
        ),
    ) {
        CompositionLocalProvider(
            LocalGradientColors provides GradientColors(DarkGreenGray95)
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.SearchChild -> SearchScreen(
                    component = child.component,
                    navigationToLogin = component::onLoginClicked,
                    navigationToPlayer = component::onPlayerClicked,
                    navigationToSettings = component::onSettingsClicked
                )

                is RootComponent.Child.CacheChild -> CacheScreen(child.component)
                is RootComponent.Child.LoginChild -> LoginScreen(
                    child.component,
                    onBack = component::onBackClicked
                )

                is RootComponent.Child.PlayerChild -> PlayerScreen(child.component)
                is RootComponent.Child.SettingsChild -> SettingsScreen(
                    child.component,
                    onBack = component::onBackClicked
                )
            }
        }
    }
}

private val RootComponent.Child.shouldDisplayBottomBar: Boolean
    get() = this is RootComponent.Child.SearchChild ||
            this is RootComponent.Child.CacheChild
private val DarkGreenGray95 = Color(0xFFF0F1EC)