package com.imcys.bilibilias.ui

import android.content.Intent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.component.AsGradientBackground
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBar
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBarItem
import com.imcys.bilibilias.feature.download.DownloadRoute
import com.imcys.bilibilias.feature.home.HomeContent
import com.imcys.bilibilias.feature.home.HomeRoute
import com.imcys.bilibilias.feature.tool.ToolContent
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.imcys.bilibilias.navigation.RootComponent
import com.imcys.bilibilias.navigation.TopLevelDestination
import kotlin.time.Duration.Companion.days

@Composable
fun AsApp(appState: AsAppState, component: RootComponent, modifier: Modifier = Modifier) {
    AsBackground {
        AsGradientBackground {
            val toasterState = rememberToasterState(appState.coroutineScope)

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()
            val message by appState.message.collectAsStateWithLifecycle()

            LaunchedEffect(isOffline) {
                if (isOffline) {
                    toasterState.show(
                        message = "⚠\uFE0F 您没有连接到互联网",
                        duration = Long.MAX_VALUE.days,
                    )
                }
            }
            LaunchedEffect(message) {
                message?.let {
                    toasterState.show(message = it.message)
                }
            }
            AsApp(
                toasterState = toasterState,
                component = component,
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun AsApp(
    toasterState: ToasterState,
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    NavigationTrackingSideEffect(component)
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
                modifier = Modifier.navigationBarsPadding()
            )
        },
        bottomBar = {
            AsBottomBar(
                component = component,
                modifier = Modifier.testTag("AsBottomBar"),
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            RootContent(component)
        }
    }
}

@Composable
private fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.HomeChild -> {
                HomeContent(child.component)
//                HomeRoute(
//                    onSalute = {
//                        val intent = Intent(context, DedicateActivity::class.java)
//                        context.startActivity(intent)
//                    },
//                    onDonation = {
//                        val intent = Intent(context, DonateActivity::class.java)
//                        context.startActivity(intent)
//                    }
//                )
            }

          is  RootComponent.Child.ToolChild -> {
              ToolContent(component = child.component)
                ToolRoute(
                    onSetting = { },
                    onPlayer = { }
                )
            }

            RootComponent.Child.DownloadChild -> {
                DownloadRoute(
                    onPlayer = { vUri, aUri -> }
                )
            }

            RootComponent.Child.UserChild -> Unit
        }
    }
}

@Composable
private fun AsBottomBar(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val stack by component.stack.subscribeAsState()
    val activeComponent = stack.active.instance
    AsNavigationBar(modifier.testTag("AsBottomBar")) {
        AsNavigationBarItem(
            TopLevelDestination.HOME,
            { activeComponent is RootComponent.Child.HomeChild },
            component::onHomeTabClicked
        )
        AsNavigationBarItem(
            TopLevelDestination.TOOL,
            { activeComponent is RootComponent.Child.ToolChild },
            component::onToolTabClicked
        )
        AsNavigationBarItem(
            TopLevelDestination.DOWNLOAD,
            { activeComponent is RootComponent.Child.DownloadChild },
            component::onDownloadTabClicked
        )
//        AsNavigationBarItem(activeComponent,{activeComponent is RootComponent.Child.UserChild},component::onHomeTabClicked)
    }
}

@Composable
private fun RowScope.AsNavigationBarItem(
    destination: TopLevelDestination,
    selected: () -> Boolean,
    onNavigation: () -> Unit
) {
    AsNavigationBarItem(
        selected = selected(),
        onClick = onNavigation,
        icon = {
            Icon(
                painter = painterResource(id = destination.unselectedIconId),
                contentDescription = null,
            )
        },
        selectedIcon = {
            Icon(
                painter = painterResource(id = destination.selectedIconId),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = {
            Text(
                stringResource(destination.iconTextId),
                color = if (selected()) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        },
        alwaysShowLabel = selected(),
        modifier = Modifier,
    )
}
