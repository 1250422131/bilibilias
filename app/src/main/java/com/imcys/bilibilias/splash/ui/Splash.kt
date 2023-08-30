package com.imcys.bilibilias.splash.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        show = true
        navController.navigate(SplashRouter.Screen.route) {
            popUpTo(SplashRouter.App.route) {
                inclusive = true
            }
        }
    }
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "app icon",
            Modifier
                .padding(top = screenHeight * .3f)
                .height(height = 120.dp),
            contentScale = ContentScale.FillWidth,
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary)
        )
        AnimatedVisibility(visible = show) {
            Text(
                text = stringResource(id = R.string.app_name),
                maxLines = 1,
                fontSize = 56.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
