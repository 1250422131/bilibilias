package com.imcys.bilibilias.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.imcys.bilibilias.R
import com.imcys.bilibilias.core.designsystem.reveal.circularReveal
import com.imcys.bilibilias.navigation.DefaultRootComponent
import com.imcys.bilibilias.startup.StartupComponent
import com.imcys.bilibilias.ui.AsApp
import com.imcys.bilibilias.ui.AsAppState
import kotlinx.coroutines.delay

@Composable
fun SplashContent(
    component: SplashComponent,
    onNavigationToLogin: () -> Unit,
    onNavigationToRoot: () -> Unit
) {
    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible.value = !isVisible.value
        delay(300)
    }
    val configuration = LocalConfiguration.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = (configuration.screenHeightDp * .3).dp)
            .circularReveal(
                isVisible.value,
                durationMillis = 1000,
                finishedListener = {
                    if (component.isLogin) {
                        onNavigationToRoot()
                    } else {
                        onNavigationToLogin()
                    }
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.mipmap.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .width(120.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.clipToBounds(),
            fontSize = 56.sp,
            color = MaterialTheme.colorScheme.primary,
            softWrap = false
        )
    }
}

@Composable
fun RootContent(
    component: StartupComponent,
    appState: AsAppState,
    componentContext: ComponentContext,
    modifier: Modifier = Modifier
) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(animator = fade() + scale()),
    ) {
        when (val child = it.instance) {
            is StartupComponent.Child.SplashChild -> SplashContent(
                child.component,
                component::onLoginClicked,
                component::onRootClicked
            )

            StartupComponent.Child.LoginChild -> Unit
            StartupComponent.Child.RootChild -> {
                val rootComponent = remember { DefaultRootComponent(componentContext.childContext("root")) }
                AsApp(appState, rootComponent)
            }
        }
    }
}
