package com.imcys.bilibilias.ui.root

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import bilibilias.ui.generated.resources.Res
import bilibilias.ui.generated.resources.not_connected
import bilibilias.ui.generated.resources.unknown_error
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.imcys.bilibilias.core.data.model.MessageData
import com.imcys.bilibilias.core.data.model.MessageType
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
import org.jetbrains.compose.resources.getString

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AsApp(
    component: RootComponent,
    appState: AsAppState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    AsTheme(false) {
        AsBackground {
            AsGradientBackground {

                val snackbarHostState = remember { SnackbarHostState() }

                val stateMessage by appState.stateMessage.collectAsState()
                LaunchedEffect(stateMessage) {
                    stateMessage?.let { message ->

                        // Text and Duration values dictated by the UI
                        val (text, duration) = getSnackbarValues(message)

                        // Determine whether user clicked action button
                        val snackBarResult = snackbarHostState.showSnackbar(
                            message = text,
                            actionLabel = message.label,
                            duration = duration,
                        ) == SnackbarResult.ActionPerformed

                        // Handle result action
                        if (snackBarResult) {
                            message.onConfirm?.invoke()
                        } else {
                            message.onDelay?.invoke()
                        }

                        // Remove Message from List
                        appState.errorMonitor.clearMessage(message)
                    }
                }
                AsApp(component, snackbarHostState, modifier, windowAdaptiveInfo)
            }
        }
    }
}

@Composable
internal fun AsApp(
    component: RootComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance
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
    ) {
        Scaffold(
            modifier = modifier,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                SnackbarHost(
                    snackbarHostState,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets.safeDrawing.exclude(
                            WindowInsets.ime,
                        ),
                    ),
                )
            },
        ) { padding ->
            Children(
                component,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = SnackbarDuration.Short,
                    ) == SnackbarResult.ActionPerformed
                },
                Modifier.padding(padding)
            )
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun Children(
    component: RootComponent,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier
) {
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
                    onBack = component::onBackClicked,
                    onShowSnackbar = onShowSnackbar,
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

private suspend fun getSnackbarValues(
    message: MessageData
): Pair<String, SnackbarDuration> {
    return when (message.type) {
        MessageType.OFFLINE -> getString(Res.string.not_connected) to SnackbarDuration.Indefinite
        is MessageType.MESSAGE -> (message.type as MessageType.MESSAGE).value to SnackbarDuration.Long
        MessageType.UNKNOWN -> getString(Res.string.unknown_error) to SnackbarDuration.Short
    }
}

private val RootComponent.Child.shouldDisplayBottomBar: Boolean
    get() = this is RootComponent.Child.SearchChild ||
            this is RootComponent.Child.CacheChild
private val DarkGreenGray95 = Color(0xFFF0F1EC)