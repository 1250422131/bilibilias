package com.imcys.authentication.login

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hjq.toast.Toaster
import com.imcys.authentication.AuthViewModel
import com.imcys.authentication.LoginAuthState
import com.imcys.authentication.R
import com.imcys.designsystem.component.AsButton
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

@Composable
internal fun LoginAuthRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginAuthState by viewModel.loginAuthUiState.collectAsStateWithLifecycle()
    LoginAuthScreen(
        onNavigateToHome = onNavigateToHome,
        viewModel::getQRCode,
        authState = loginAuthState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginAuthScreen(
    onNavigateToHome: () -> Unit,
    刷新二维码: () -> Unit,
    authState: LoginAuthState,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    SideEffect {
        if (authState.isSuccess) {
            onNavigateToHome()
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_dialog_login_qr_bottomsheet_title),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        snackbarHost = {
            LaunchedEffect(authState.snackBarMessage) {
                authState.snackBarMessage?.let { snackbarHostState.showSnackbar(it) }
            }
            SnackbarHost(snackbarHostState) { data ->
                Snackbar { Text(data.visuals.message) }
            }
        }
    ) { innerPadding ->
        Column(
            modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            val qrCodePainter = rememberQrCodePainter(authState.qrCodeUrl)
            AsyncImage(
                model = qrCodePainter,
                onSuccess = {
                    saveQRCode(it.result.drawable.toBitmap(), context)
                },
                contentDescription = "二维码",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clickable(onClick = 刷新二维码)
            )
            Text(
                text = authState.qrCodeMessage,
                Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
            )
            // region 跳转扫码
            AsButton(
                onClick = { goToQRScan(context) },
                Modifier
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .height(60.dp)
                    .fillMaxWidth(),
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

private fun saveQRCode(bitmap: Bitmap, context: Context) {
    val bili =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "bili")
    try {
        val photo = File(bili, "BILIBILIAS-QR-Code.jpg")
        photo.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, out)
            out.flush()
        }
        MediaScannerConnection.scanFile(
            context,
            arrayOf(photo.toString()),
            null
        ) { _, _ ->
            Toaster.show(R.string.app_LoginQRModel_downloadLoginQR_asToast)
        }
    } catch (e: FileNotFoundException) {
        Timber.d(e)
    } catch (e: IOException) {
        Timber.d(e)
    } finally {
        goToQRScan(context)
    }
}

private fun goToQRScan(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
    val packageManager = context.packageManager
    val componentName = intent.resolveActivity(packageManager)
    if (componentName != null) {
        context.startActivity(intent)
    } else {
        Toaster.show(R.string.app_LoginQRModel_goToQR)
    }
}
