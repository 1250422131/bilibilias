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
import com.imcys.bilibilias.core.designsystem.reveal.circularReveal
import kotlinx.coroutines.delay

@Composable
fun SplashContent(
    component: SplashComponent,
    navigationToLogin: () -> Unit,
    navigationToTool: () -> Unit,
) {
    if (component.isLogin) {
        navigationToTool()
    } else {
        navigationToLogin()
    }
}
