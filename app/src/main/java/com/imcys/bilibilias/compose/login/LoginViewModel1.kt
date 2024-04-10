package com.imcys.bilibilias.compose.login

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.hjq.toast.Toaster
import com.imcys.bilibilias.R
import com.imcys.bilibilias.core.common.utils.updatePhotoMedias
import com.imcys.bilibilias.core.network.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoginViewModel1 @Inject constructor(
    private val loginRepository: LoginRepository,
    @ApplicationContext context: Context,
) :
    ViewModel() {
    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    init {
        applyQrCode(context)
    }

    fun applyQrCode(context: Context) {
        viewModelScope.launch {
            val response = loginRepository.获取二维码()
            _loginUiState.update { it.copy(qrCodeUrl = response.url) }
            saveImage(response.url, context)
            tryLogin(response.qrcodeKey)
        }
    }

    private fun saveImage(url: String, context: Context) {
        val drawable = genQrCode(url)
        val bitmap = drawable.toBitmap(1000, 1000)
            .addWhiteBorder(200)
        saveQRCode(bitmap, context)
    }

    private suspend fun tryLogin(key: String) {
        withTimeout(3.minutes) {
            var ok = false
            while (isActive && !ok) {
                delay(1.seconds)
                val response = loginRepository.轮询登录(key)
                _loginUiState.update {
                    it.copy(
                        message = response.message,
                        success = response.success
                    )
                }
                ok = response.success
            }
        }
    }
}

internal fun genQrCode(url: String): Drawable {
    val data = QrData.Url(url)
    return QrCodeDrawable(data)
}

private fun saveQRCode(bitmap: Bitmap, context: Context) {
    val bili =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "bili")
    try {
        val photo = File(bili, "BILIBILIAS-QR-Code.png")
        photo.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        updatePhotoMedias(context, photo)
    } catch (_: FileNotFoundException) {
    } catch (_: IOException) {
    } finally {
        goToQRScan(context)
    }
}

private fun goToQRScan(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val packageManager = context.packageManager
    val componentName = intent.resolveActivity(packageManager)
    if (componentName != null) {
        context.startActivity(intent)
    } else {
        Toaster.show(R.string.app_LoginQRModel_goToQR)
    }
}

private fun Bitmap.addWhiteBorder(borderSize: Int): Bitmap {
    val bmpWithBorder = Bitmap.createBitmap(
        getWidth() + borderSize * 2,
        getHeight() + borderSize * 2,
        getConfig()
    )
    val canvas = Canvas(bmpWithBorder)
    canvas.drawColor(Color.WHITE)
    val target = RectF(
        borderSize.toFloat(),
        borderSize.toFloat(),
        (width + borderSize).toFloat(),
        (height + borderSize).toFloat()
    )
    canvas.drawBitmap(this, null, target, null)
    return bmpWithBorder
}
