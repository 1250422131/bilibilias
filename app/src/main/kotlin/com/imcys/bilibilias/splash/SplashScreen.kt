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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.imcys.bilibilias.R
import com.imcys.bilibilias.core.designsystem.reveal.circularReveal
import com.imcys.bilibilias.ui.MainScreen
import com.sockmagic.login.LoginScreen
import kotlinx.coroutines.delay

object SplashScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: SplashViewModel = getViewModel()
        SplashContent(viewModel.isLogin)
    }
}

@Composable
fun SplashContent(login: Boolean) {
    val configuration = LocalConfiguration.current
    val isVisible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible.value = !isVisible.value
        delay(300)
    }
    val navigator = LocalNavigator.currentOrThrow
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = (configuration.screenHeightDp * .3).dp)
            .circularReveal(
                isVisible.value,
                durationMillis = 1000,
                finishedListener = {
                    val mainScreen = MainScreen()
                    if (login) {
                        navigator.replaceAll(mainScreen)
                    } else {
                        navigator.push(LoginScreen({ mainScreen }))
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