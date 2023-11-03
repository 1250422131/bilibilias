package com.imcys.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.datastore.fastkv.CookiesData
import com.imcys.network.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val cookiesData: CookiesData,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _loginAuthState = MutableStateFlow(LoginAuthState())
    val loginAuthUiState = _loginAuthState.asStateFlow()

    init {
        getQRCode()
    }

    fun getQRCode() {
        viewModelScope.launch(Dispatchers.IO) {
            val (qrcodeKey, url) = loginRepository.getQRCode()
            _loginAuthState.update { it.copy(qrCodeUrl = url) }
            tryLogin(qrcodeKey)
        }
    }

    fun downloadQRCode(bitmap: Bitmap, photoName: String, context: Context) {
        viewModelScope.launch(ioDispatcher) {
            val bili = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "bili")
            try {
                val photo = File(bili, photoName)
                photo.outputStream().use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, out)
                    out.flush()
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
            } catch (e: FileNotFoundException) {
                Timber.tag(TAG).d(e)
            } catch (e: IOException) {
                Timber.tag(TAG).d(e)
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
    private suspend fun tryLogin(key: String) {
        withTimeout(3.minutes) {
            while (isActive) {
                delay(1.seconds)
                val response = loginRepository.pollLogin(key)
                _loginAuthState.update {
                    it.copy(
                        qrCodeMessage = response.message,
                        isSuccess = response.isSuccess,
                        snackBarMessage = if (response.isSuccess) "登录成功" else null
                    )
                }
                cookiesData.refreshToken = response.refreshToken
                Timber.d("polling=${response}")
                if (response.isSuccess) break
            }
        }
    }
}

private const val TAG = "AuthViewModel"
private const val QR_CODE_URL = "qrCodeUrl"

data class LoginAuthState(
    val qrCodeUrl: String = "",

    val qrCodeMessage: String = "",
    val isSuccess: Boolean = false,

    val snackBarMessage: String? = null,
)
