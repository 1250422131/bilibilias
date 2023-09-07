package com.imcys.bilibilias.ui.authentication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import com.imcys.bilibilias.R
import com.imcys.bilibilias.base.model.login.AuthQrCodeBean
import com.imcys.bilibilias.base.model.login.LoginResponseBean
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.request
import io.ktor.http.encodeURLParameter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val http: HttpClient) : BaseViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    init {
        applyQRCode()
    }

    private fun applyQRCode() {
        launchIO {
            val bean = http.get(BilibiliApi.getLoginQRPath)
                .body<AuthQrCodeBean.Data>()
            _authState.update {
                it.copy(
                    qrCodeUrl = "https://pan.misakamoe.com/qrcode/?url=${bean.url.encodeURLParameter()}",
                    qrCodeKey = bean.qrcodeKey
                )
            }
            Timber.tag("qrCode").d("%s", _authState.value.qrCodeUrl)
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
                context, arrayOf(photo.toString()), null
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

    fun completeSigning() {
        launchIO {
            val bean1 = http.get(BilibiliApi.getLoginStatePath) {
                url {
                    parameters.append("qrcode_key", _authState.value.qrCodeKey)
                }
            }
            Timber.tag(TAG).d(bean1.request.url.toString())
            val bean = bean1.body<LoginResponseBean>()
            Timber.tag(TAG).d(bean.toString())
        }
    }
}

private const val TAG = "AuthViewModel"

data class AuthState(
    val qrCodeUrl: String = "",
    val qrCodeKey: String = "",
    val snackBarMessage: String = "",
)