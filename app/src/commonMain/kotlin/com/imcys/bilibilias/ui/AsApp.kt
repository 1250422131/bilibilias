package com.imcys.bilibilias.ui

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
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.navigation3.runtime.EntryProviderBuilder
import bilibilias.app.generated.resources.not_connected
import bilibilias.app.generated.resources.unknown_error
import com.imcys.bilibilias.core.data.model.MessageData
import com.imcys.bilibilias.core.data.model.MessageType
import com.imcys.bilibilias.core.navigation.AsNavKey
import com.imcys.bilibilias.navigation.AsNavDisplay
import com.imcys.bilibilias.ui.component.AsBackground
import com.imcys.bilibilias.ui.component.AsGradientBackground
import com.imcys.bilibilias.ui.component.AsNavigationSuiteScaffold
import org.jetbrains.compose.resources.getString

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AsApp(
    appState: AsAppState,
    entryProviderBuilders: EntryProviderBuilder<AsNavKey>.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    AsBackground(modifier = modifier) {
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
            CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
                AsApp(
                    appState = appState,
                    snackbarHostState = snackbarHostState,
                    entryProviderBuilders = entryProviderBuilders,
                    windowAdaptiveInfo = windowAdaptiveInfo
                )
            }
        }
    }
}

// TODO: move to common
val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState state should be initialized at runtime")
}

@Composable
internal fun AsApp(
    appState: AsAppState,
    snackbarHostState: SnackbarHostState,
    entryProviderBuilders: EntryProviderBuilder<AsNavKey>.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    val currentTopLevelKey = appState.currentTopLevelDestination!!.key

    AsNavigationSuiteScaffold(
        navigationSuiteItems = {
            appState.topLevelDestinations.forEach { destination ->
                val selected = destination.key == currentTopLevelKey
                item(
                    selected = selected,
                    onClick = { appState.asBackStack.navigate(destination.key) },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = null,
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = { Text(destination.iconText) },
                    modifier = Modifier
                        .testTag("NiaNavItem")
                )
            }
        },
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
            AsNavDisplay(
                asBackStack = appState.asBackStack,
                entryProviderBuilders,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

//@OptIn(ExperimentalDecomposeApi::class)
//@Composable
//private fun Children(
//    component: RootComponent,
//    onShowSnackbar: suspend (String, String?) -> Boolean,
//    modifier: Modifier = Modifier
//) {
//    Children(
//        stack = component.stack,
//        modifier = modifier,
//        animation = predictiveBackAnimation(
//            backHandler = component.backHandler,
//            fallbackAnimation = stackAnimation(slide()),
//            onBack = component::onBackClicked,
//        ),
//    ) {
//        CompositionLocalProvider(
//            LocalGradientColors provides GradientColors(DarkGreenGray95)
//        ) {
//            when (val child = it.instance) {
//                is RootComponent.Child.SearchChild -> SearchScreen(
//                    component = child.component,
//                    navigationToLogin = component::onLoginClicked,
//                    navigationToPlayer = component::onPlayerClicked,
//                    navigationToSettings = component::onSettingsClicked
//                )
//
//                is RootComponent.Child.CacheChild -> CacheScreen(child.component)
//                is RootComponent.Child.LoginChild -> LoginScreen(
//                    child.component,
//                    onBack = component::onBackClicked,
//                    onShowSnackbar = onShowSnackbar,
//                )
//
//                is RootComponent.Child.PlayerChild -> PlayerScreen(child.component)
//                is RootComponent.Child.SettingsChild -> SettingsScreen(
//                    child.component,
//                    onBack = component::onBackClicked
//                )
//            }
//        }
//    }
//}

private suspend fun getSnackbarValues(
    message: MessageData
): Pair<String, SnackbarDuration> {
    return when (message.type) {
        MessageType.OFFLINE -> getString(bilibilias.app.generated.resources.Res.string.not_connected) to SnackbarDuration.Indefinite
        is MessageType.MESSAGE -> (message.type as MessageType.MESSAGE).value to SnackbarDuration.Long
        MessageType.UNKNOWN -> getString(bilibilias.app.generated.resources.Res.string.unknown_error) to SnackbarDuration.Short
    }
}

private val DarkGreenGray95 = Color(0xFFF0F1EC)