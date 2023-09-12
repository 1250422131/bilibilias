package com.imcys.bilibilias.splash.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.common.base.components.BottomSheetDialog
import com.imcys.bilibilias.common.base.config.CookieRepository
import com.imcys.bilibilias.permission.findActivity
import com.imcys.bilibilias.permission.gotoApplicationSettings
import com.imcys.bilibilias.permission.hasPickMediaPermission
import com.imcys.bilibilias.permission.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun Splash(navController: NavHostController) {
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        show = true
        delay(1200)
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
        Text(
            text = stringResource(id = R.string.app_name),
            maxLines = 1,
            fontSize = 56.sp,
            softWrap = false,
            color = MaterialTheme.colorScheme.primary
        )
    }
    Box(Modifier.fillMaxSize()) { CheckPermission(navController) }
}

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermission(navController: NavHostController) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val permissionState = rememberPermissionState(Manifest.permission.WRITE_EXTERNAL_STORAGE) { granted ->
        if (!granted) {
            context.findActivity()?.apply {
                when {
                    shouldShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        showDialog = true
                    }

                    else -> {
                        context.gotoApplicationSettings()
                    }
                }
            }
        }
    }
    LaunchedEffect(permissionState.status.isGranted) {
        if (context.hasPickMediaPermission()) {
            if (CookieRepository.isExpired) {
                navController.navigate(SplashRouter.AuthMethod.route) {
                    popUpTo(SplashRouter.App.route) {
                        inclusive = true
                    }
                }
            } else {
                navController.navigate(SplashRouter.Screen.route) {
                    popUpTo(SplashRouter.App.route) {
                        inclusive = true
                    }
                }
            }
        } else {
            showDialog = true
        }
    }

    BottomSheetDialog(
        visible = showDialog,
        Modifier.fillMaxSize(),
        onDismissRequest = {},
        cancelable = false,
        canceledOnTouchOutside = false
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.app_permission_application_title),
                Modifier.height(40.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                Modifier.padding(horizontal = 25.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.app_permission_application_msg),
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = {
                    scope.launch {
                        permissionState.launchPermissionRequest()
                    }
                },
                Modifier
                    .height(60.dp)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text(stringResource(R.string.app_permission_application_confirm))
            }
            Button(
                onClick = {
                    context.gotoApplicationSettings()
                    context.findActivity()?.finish()
                },
                Modifier
                    .height(60.dp)
                    .padding(top = 24.dp),
                colors = ButtonDefaults.buttonColors(),
            ) {
                Text(stringResource(R.string.app_permission_application_cancel))
            }
            Spacer(modifier = Modifier.height(256.dp))
        }
    }
}
