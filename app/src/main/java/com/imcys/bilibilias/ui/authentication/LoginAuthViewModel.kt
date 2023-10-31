package com.imcys.bilibilias.ui.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.imcys.bilibilias.R
import com.imcys.network.LoginRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoginAuthViewModel @Inject constructor(private val loginRepository: com.imcys.network.LoginRepository) : BaseViewModel() {

    private val _loginAuthState = MutableStateFlow(LoginAuthState())
    val loginAuthUiState = _loginAuthState.asStateFlow()

    init {
        getQRCode()
    }

    fun getQRCode() {
        launchIO {
            loginRepository.getQrCode { data ->
                _loginAuthState.update {
                    it.copy(qrCodeUrl = data.url)
                }
                tryLogin(data.qrcodeKey)
            }
        }
    }

    fun downloadQRCode(bitmap: Bitmap, photoName: String, context: Context) {
        launchIO {
            val photoDir = File(Environment.getExternalStorageDirectory(), "BILIBILIAS")
            if (!photoDir.exists()) {
                photoDir.mkdirs()
            }
            val photo = File(photoDir, photoName)
            try {
                photo.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, out)
                    out.flush()
                }
            } catch (e: FileNotFoundException) {
                Timber.tag(TAG).d(e)
            } catch (e: IOException) {
                Timber.tag(TAG).d(e)
            }

            MediaScannerConnection.scanFile(
                context,
                arrayOf(photo.toString()),
                null
            ) { path, uri ->
                _loginAuthState.update {
                    it.copy(snackBarMessage = context.getString(R.string.app_LoginQRModel_downloadLoginQR_asToast))
                }
                Timber.tag(TAG).d("path=$path, uri=$uri")
            }
        }
        goToBilibiliQRScan(context)
    }

    fun goToBilibiliQRScan(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
        val packageManager = context.packageManager
        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null) {
            context.startActivity(intent)
        } else {
            _loginAuthState.update {
                it.copy(snackBarMessage = context.getString(R.string.app_LoginQRModel_goToQR))
            }
        }
    }

    /**
     * 0：扫码登录成功
     * 86038：二维码已失效
     * 86090：二维码已扫码未确认
     * 86101：未扫码
     */
    private fun tryLogin(key: String) {
        launchIO {
            val seconds = 1.seconds
            withTimeout(3.minutes) {
                while (isActive) {
                    delay(seconds)
                    var success = false
                    loginRepository.onPollingLogin(key) { data ->
                        success = data.isSuccess
                        _loginAuthState.update {
                            it.copy(
                                qrCodeMessage = data.message,
                                isSuccess = success,
                                snackBarMessage = if (success) "登录成功" else null
                            )
                        }
                    }
                    if (success) break
                }
            }
        }
    }
}

private const val TAG = "AuthViewModel"

data class LoginAuthState(
    val qrCodeUrl: String = "",

    val qrCodeMessage: String = "",
    val isSuccess: Boolean = false,

    val snackBarMessage: String? = null,
)
