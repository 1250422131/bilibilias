package com.imcys.bilibilias.ui.authentication

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.router.SplashRouter
import com.imcys.bilibilias.common.base.components.FullScreenScaffold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val authStateState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    FullScreenScaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                stringResource(R.string.app_dialog_login_qr_bottomsheet_title),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        })
    }, snackbarHost = {
        LaunchedEffect(authStateState.snackBarMessage) {
            val message = authStateState.snackBarMessage
            if (message.isNotBlank()) {
                snackbarHostState.showSnackbar(authStateState.snackBarMessage)
            }
        }
        SnackbarHost(snackbarHostState) { data ->
            Snackbar { Text(data.visuals.message) }
        }
    }) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var bitmap by remember { mutableStateOf<Bitmap?>(null) }
            SubcomposeAsyncImage(
                model = authStateState.qrCodeUrl,
                loading = {
                    CircularProgressIndicator()
                },
                onSuccess = {
                    bitmap = it.result.drawable.toBitmap()
                },
                contentDescription = "qr code",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(130.dp)
            )
            Text(
                text = "未扫码",
                Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            CompleteLoginButton {
                authViewModel.completeSigning()
                navController.navigate(SplashRouter.Screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
            val photoName = stringResource(R.string.app_LoginQRModel_downloadLoginQR_fileName)
            DownloadQrCodeButton {
                if (bitmap != null) {
                    authViewModel.downloadQrCode(bitmap!!, photoName, context)
                }
                bitmap = null
            }
            JumpScanCodeButton {
                authViewModel.goToBilibiliQrScan(context)
            }
        }
    }
}

@Composable
private fun JumpScanCodeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .height(60.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = stringResource(R.string.app_dialog_login_qr_bottomsheet_go),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DownloadQrCodeButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .height(60.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.Black
        )
    ) {
        Text(
            text = stringResource(R.string.app_dialog_login_qr_bottomsheet_download),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CompleteLoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        Modifier
            .padding(horizontal = 25.dp, vertical = 10.dp)
            .height(60.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.app_dialog_login_qr_bottomsheet_finish),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
