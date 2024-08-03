package com.imcys.bilibilias.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imcys.bilibilias.R
import com.imcys.bilibilias.permission.CheckPermissionDialog

@Composable
fun SplashRoute(navigateToAuthMethod: () -> Unit, navigateToHome: () -> Unit) {
    val viewModel: SplashViewModel = hiltViewModel()
    val valid = viewModel.valid
    SplashScreen(navigateToAuthMethod, navigateToHome, valid)
}

@Composable
fun SplashScreen(navigateToAuthMethod: () -> Unit, navigateToHome: () -> Unit, valid: Boolean) {
    Column(
        Modifier
            .fillMaxSize(),
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
        Text(
            text = stringResource(id = R.string.app_name),
            maxLines = 1,
            fontSize = 56.sp,
            softWrap = false,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Box(Modifier.height(600.dp)) {
        CheckPermissionDialog(
            navigateToAuthMethod,
            navigateToHome,
            valid
        )
    }
}
