package com.imcys.bilibilias.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.dokar.sonner.ToastType
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.imbys.bilibilias.feature.authorspace.AuthorSpaceContent
import com.imcys.bilibilias.core.data.util.MessageType
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.component.AsGradientBackground
import com.imcys.bilibilias.core.designsystem.component.AsNavigationSuiteScaffold
import com.imcys.bilibilias.core.designsystem.component.AsNavigationSuiteScope
import com.imcys.bilibilias.core.designsystem.theme.GradientColors
import com.imcys.bilibilias.core.designsystem.theme.LocalGradientColors
import com.imcys.bilibilias.feature.download.DownloadContent
import com.imcys.bilibilias.feature.home.HomeContent
import com.imcys.bilibilias.feature.login.LoginContent
import com.imcys.bilibilias.feature.player.PlayerContent
import com.imcys.bilibilias.feature.settings.SettingContent
import com.imcys.bilibilias.feature.splash.SplashContent
import com.imcys.bilibilias.feature.tool.ToolContent
import com.imcys.bilibilias.navigation.RootComponent
import com.imcys.bilibilias.navigation.TopLevelDestination

@Composable
fun AsApp(
    appState: AsAppState,
    component: RootComponent,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val shouldShowGradientBackground =
        component.currentTopLevelDestination == TopLevelDestination.TOOL
    AsBackground(modifier = modifier) {
        AsGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val snackbarMessage by appState.snackbarMessage.collectAsStateWithLifecycle()
            val toasterState = rememberToasterState(appState.coroutineScope) {
                snackbarMessage?.actionPerformed?.invoke()
            }
            SideEffect {
                appState.offlineMessage = "⚠\uFE0F 您没有连接到互联网"
            }
            LaunchedEffect(snackbarMessage) {
                snackbarMessage?.let {
                    toasterState.show(
                        message = it.message,
                        id = it.id,
                        type = toastMessageTypeOf(it.messageType),
                        duration = it.duration,
                    )

                    // Remove Message from Queue
                    appState.clearErrorMessage(it.id)
                }
            }

            AsApp(
                toasterState = toasterState,
                component = component,
                modifier = modifier,
                windowAdaptiveInfo = windowAdaptiveInfo,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AsApp(
    toasterState: ToasterState,
    component: RootComponent,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance
    NavigationTrackingSideEffect(activeComponent)
    AsBackHandler(
        component.backHandler,
        component.currentTopLevelDestination,
        component.currentDestination,
        component::onBack,
    )
    AsNavigationSuiteScaffold(
        navigationSuiteItems = {
            item(
                selected = activeComponent is RootComponent.Child.HomeChild,
                onClick = component::onHomeTabClicked,
                destination = TopLevelDestination.HOME,
            )
            item(
                selected = activeComponent is RootComponent.Child.ToolChild,
                onClick = component::onToolTabClicked,
                destination = TopLevelDestination.TOOL,
            )
            item(
                selected = activeComponent is RootComponent.Child.DownloadChild,
                onClick = component::onDownloadTabClicked,
                destination = TopLevelDestination.DOWNLOAD,
            )
        },
        shouldShowBottomBar = component.shouldShowBottomBar,
        modifier = Modifier.testTag("AsNavItem"),
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.semantics {
                testTagsAsResourceId = true
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = {
                Toaster(
                    state = toasterState,
                    alignment = Alignment.TopCenter,
                )
            },
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                Box(modifier = Modifier.consumeWindowInsets(WindowInsets(0, 0, 0, 0))) {
                    RootContent(component)
                }
            }
        }
    }
}

@Composable
private fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation { _ -> slide() + fade() },
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.HomeChild ->
                HomeContent(component = child.component)

            is RootComponent.Child.ToolChild ->
                ToolContent(
                    component = child.component,
                    navigationToSettings = component::onSettingsTabClicked,
                    navigationToAuthorSpace = component::onAuthorSpaceTabClicked,
                )

            is RootComponent.Child.DownloadChild ->
                DownloadContent(
                    component = child.component,
                    navigationToPlayer = component::onPlayedTabClicked,
                )

            RootComponent.Child.UserChild -> Unit
            is RootComponent.Child.PlayerChild -> PlayerContent(component = child.component)
            is RootComponent.Child.LoginChild -> LoginContent(
                component = child.component,
                navigationToTool = component::onToolTabClicked,
            )

            is RootComponent.Child.SplashChild -> SplashContent(
                component = child.component,
                navigationToLogin = component::onLoginTabClicked,
                navigationToTool = component::onHomeTabClicked,
            )

            is RootComponent.Child.SettingsChild -> SettingContent(component = child.component)
            is RootComponent.Child.AuthorSpaceChild -> AuthorSpaceContent(component = child.component)
        }
    }
}

private fun AsNavigationSuiteScope.item(
    selected: Boolean,
    onClick: () -> Unit,
    destination: TopLevelDestination,
    modifier: Modifier = Modifier,
) {
    item(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        icon = {
            Icon(
                painterResource(destination.unselectedIconId),
                contentDescription = null,
            )
        },
        selectedIcon = {
            Icon(
                painterResource(destination.selectedIconId),
                contentDescription = null,
            )
        },
        label = { Text(stringResource(destination.iconTextId)) },
    )
}

private fun toastMessageTypeOf(type: MessageType): ToastType = when (type) {
    MessageType.Normal -> ToastType.Normal
    MessageType.Success -> ToastType.Success
    MessageType.Info -> ToastType.Info
    MessageType.Warning -> ToastType.Warning
    MessageType.Error -> ToastType.Error
}
