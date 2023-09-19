package com.imcys.bilibilias.ui.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.repository.LoginRepository
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.http.encodeURLParameter
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
class AuthViewModel @Inject constructor(private val loginRepository: LoginRepository) : BaseViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    init {
        applyQRCode()
    }

    private fun applyQRCode() {
        launchIO {
            loginRepository.applyForQrCode { data ->
                val state = _authState.update {
                    it.copy(
                        qrCodeUrl = "https://pan.misakamoe.com/qrcode/?url=${data.url.encodeURLParameter()}",
                    )
                }
                tryLogin(data.qrcodeKey)
            }
        }
    }

    fun downloadQrCode(bitmap: Bitmap, photoName: String, context: Context) {
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
                _authState.update {
                    it.copy(snackBarMessage = context.getString(R.string.app_LoginQRModel_downloadLoginQR_asToast))
                }
                Timber.tag(TAG).d("path=$path, uri=$uri")
            }
        }
        goToBilibiliQrScan(context)
    }

    fun goToBilibiliQrScan(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bilibili://qrscan"))
        val packageManager = context.packageManager
        val componentName = intent.resolveActivity(packageManager)
        if (componentName != null) {
            context.startActivity(intent)
        } else {
            _authState.update {
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
            withTimeout(3.minutes) {
                while (isActive) {
                    delay(1.seconds)
                    var canLogin = false
                    loginRepository.pollingLogin(key)
                        .onSuccess { state ->
                            Timber.tag("tryLogin").d(state.toString())
                            canLogin = canLogin(state.code)
                            _authState.update {
                                it.copy(
                                    loginStateMessage = state.message,
                                    loginState = canLogin
                                )
                            }
                        }
                        .onFailure {
                            Timber.tag("tryLogin").d(it)
                        }
                    if (canLogin) break
                }
            }
        }
    }

    private fun canLogin(code: Int) = code == 0
}

private const val TAG = "AuthViewModel"

data class AuthState(
    val qrCodeUrl: String = "",

    val loginStateMessage: String = "",
    val loginState: Boolean = false,

    val snackBarMessage: String? = null,
)
