package com.imcys.bilibilias.ui

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import androidx.tracing.trace
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.imcys.bilibilias.MainActivityViewModel
import com.imcys.bilibilias.core.common.utils.getActivity
import com.imcys.bilibilias.core.designsystem.component.AsBackground
import com.imcys.bilibilias.core.designsystem.component.AsGradientBackground
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBar
import com.imcys.bilibilias.core.designsystem.component.AsNavigationBarItem
import com.imcys.bilibilias.feature.download.DownloadRoute
import com.imcys.bilibilias.feature.home.HomeRoute
import com.imcys.bilibilias.feature.tool.ToolRoute
import com.imcys.bilibilias.home.ui.activity.DedicateActivity
import com.imcys.bilibilias.home.ui.activity.DonateActivity
import com.imcys.bilibilias.navigation.RootComponent
import kotlin.time.Duration.Companion.days

class MainScreen : Screen {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Content() {
        val viewModel: MainActivityViewModel = getViewModel()
        val activity = LocalContext.current.getActivity()
        val appState = rememberNiaAppState(
            viewModel.toastMachine,
            networkMonitor = viewModel.networkMonitor,
            windowSizeClass = calculateWindowSizeClass(activity)
        )
//        AsApp(appState)
    }
}

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
    ) { child ->
        when (child.instance) {
            RootComponent.Child.HomeChild -> {
                val context = LocalContext.current
                HomeRoute(
                    onSalute = {
                        val intent = Intent(context, DedicateActivity::class.java)
                        context.startActivity(intent)
                    },
                    onDonation = {
                        val intent = Intent(context, DonateActivity::class.java)
                        context.startActivity(intent)
                    }
                )
            }

            RootComponent.Child.ToolChild -> {
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
            RootComponent.Child.HomeChild.unselectedIcon,
            RootComponent.Child.HomeChild.selectedIcon,
            RootComponent.Child.HomeChild.title,
            { activeComponent is RootComponent.Child.HomeChild },
            component::onHomeTabClicked
        )
        AsNavigationBarItem(
            RootComponent.Child.ToolChild.unselectedIcon,
            RootComponent.Child.ToolChild.selectedIcon,
            RootComponent.Child.ToolChild.title,
            { activeComponent is RootComponent.Child.ToolChild },
            component::onToolTabClicked
        )
        AsNavigationBarItem(
            RootComponent.Child.DownloadChild.unselectedIcon,
            RootComponent.Child.DownloadChild.selectedIcon,
            RootComponent.Child.DownloadChild.title,
            { activeComponent is RootComponent.Child.DownloadChild },
            component::onDownloadTabClicked
        )
//        AsNavigationBarItem(activeComponent,{activeComponent is RootComponent.Child.UserChild},component::onHomeTabClicked)
    }
}

@Composable
private fun RowScope.AsNavigationBarItem(
    @DrawableRes unselectedIcon: Int,
    @DrawableRes selectedIcon: Int,
    @StringRes title: Int,
    selected: () -> Boolean,
    onNavigation: () -> Unit
) {
    AsNavigationBarItem(
        selected = selected(),
        onClick = onNavigation,
        icon = {
            Icon(
                painter = painterResource(id = unselectedIcon),
                contentDescription = null,
            )
        },
        selectedIcon = {
            Icon(
                painter = painterResource(id = selectedIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = {
            Text(
                stringResource(title),
                color = if (selected()) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        },
        alwaysShowLabel = selected(),
        modifier = Modifier,
    )
}
