package com.imcys.bilibilias.feature.splash

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.imcys.bilibilias.core.designsystem.reveal.circularReveal
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
            painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .width(120.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Text(
            text = "BILIBILIAS",
            modifier = Modifier,
            fontSize = 56.sp,
            color = MaterialTheme.colorScheme.primary,
            softWrap = false
        )
    }
}
