package com.imcys.bilibilias.ui.splash

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.imcys.bilibilias.R
import com.imcys.bilibilias.permission.CheckPermissionDialog
import com.imcys.designsystem.component.FullScreenScaffold
import kotlinx.coroutines.delay

const val ROUTE_SPLASH = "splash"

@RequiresApi(Build.VERSION_CODES.M)
fun NavGraphBuilder.splashRoute(
    navigateToAuthMethod: () -> Unit,
    navigateToHome: () -> Unit,
) = composable(ROUTE_SPLASH) {
    val viewModel: SplashViewModel = hiltViewModel()
    SplashRoute(
        navigateToAuthMethod,
        navigateToHome,
        viewModel.login
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun SplashRoute(navigateToAuthMethod: () -> Unit, navigateToHome: () -> Unit, login: Boolean) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        show = true
        delay(1200)
    }
    FullScreenScaffold(
        Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
            // todo bug fix
            // Text(
            //     text = stringResource(id = R.string.app_name),
            //     maxLines = 1,
            //     fontSize = 56.sp,
            //     softWrap = false,
            //     color = MaterialTheme.colorScheme.primary
            // )
        }
    }
    Box(Modifier.height(600.dp)) { CheckPermissionDialog(navigateToAuthMethod, navigateToHome, login) }
}
