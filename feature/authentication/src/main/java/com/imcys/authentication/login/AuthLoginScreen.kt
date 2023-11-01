package com.imcys.authentication.login

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import coil.compose.SubcomposeAsyncImage
import com.imcys.authentication.LoginAuthState
import com.imcys.authentication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginAuthScreen(
    onNavigateToHome: () -> Unit,
    onGetQRCode: () -> Unit,
    onDownloadQRCode: (Bitmap, String, Context) -> Unit,
    onGoToBiliBiliQRScan: (Context) -> Unit,
    loginAuthState: LoginAuthState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                stringResource(R.string.app_dialog_login_qr_bottomsheet_title),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        })
    }, snackbarHost = {
        LaunchedEffect(loginAuthState.snackBarMessage) {
            loginAuthState.snackBarMessage?.let { snackbarHostState.showSnackbar(it) }
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
                model = loginAuthState.qrCodeUrl,
                loading = {
                    CircularProgressIndicator()
                },
                onSuccess = {
                    bitmap = it.result.drawable.toBitmap()
                },
                contentDescription = "二维码",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clickable(onClick = onGetQRCode)
            )
            Text(
                text = loginAuthState.qrCodeMessage,
                Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            SideEffect {
                if (loginAuthState.isSuccess) {
                    onNavigateToHome()
                }
            }
            // region 下载二维码
            val photoName = stringResource(R.string.app_LoginQRModel_downloadLoginQR_fileName)
            val context = LocalContext.current
            Button(
                onClick = {
                    bitmap?.let {
                        onDownloadQRCode(it, photoName, context)
                    }
                },
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
            // endregion
            // region 跳转扫码
            Button(
                onClick = { onGoToBiliBiliQRScan(context) },
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
            // endregion
        }
    }
}
