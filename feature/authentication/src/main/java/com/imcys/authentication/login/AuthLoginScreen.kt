package com.imcys.authentication.login

import android.content.*
import android.graphics.*
import android.net.*
import android.os.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.*
import androidx.lifecycle.compose.*
import com.hjq.toast.*
import com.imcys.authentication.*
import com.imcys.authentication.R
import com.imcys.common.utils.*
import com.imcys.designsystem.component.*
import io.github.alexzhirkevich.qrose.*
import timber.log.*
import java.io.*

@Composable
internal fun LoginAuthRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: AuthViewModel = hiltViewModel()
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
    getQrCode: () -> Unit,
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
            val qrCodePainter = rememberQrCodePainter(authState.qrCodeUrl)
            Image(
                qrCodePainter,
                contentDescription = "二维码",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clickable(onClick = getQrCode)
            )
            Text(
                text = authState.qrCodeMessage,
                Modifier.padding(top = 12.dp),
                fontWeight = FontWeight.Bold,
            )
            // region 跳转扫码
            val context = LocalContext.current
            AsButton(
                onClick = {
                    val bitmap = getWindowBitmapPath(context)
                    saveQRCode(bitmap, context, true)
                },
                Modifier
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .height(60.dp)
                    .fillMaxWidth()
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

fun getWindowBitmapPath(context: Context): Bitmap {
    val view = context.getActivity().window?.decorView?.rootView
    val bitmap = Bitmap.createBitmap(
        view?.width ?: 0,
        view?.height?.div(2) ?: 0,
        Bitmap.Config.ARGB_8888
    )

    view?.draw(Canvas(bitmap))
    return bitmap
}

private fun saveQRCode(bitmap: Bitmap, context: Context, recycle: Boolean) {
    val bili =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "bili")
    try {
        val photo = File(bili, "BILIBILIAS-QR-Code.png")
        photo.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
        }
        updatePhotoMedias(context, photo)
    } catch (e: FileNotFoundException) {
        Timber.d(e)
    } catch (e: IOException) {
        Timber.d(e)
    } finally {
        if (recycle && !bitmap.isRecycled) {
            bitmap.recycle()
        }
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
