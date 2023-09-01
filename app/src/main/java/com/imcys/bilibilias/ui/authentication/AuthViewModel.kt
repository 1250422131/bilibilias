package com.imcys.bilibilias.ui.authentication

import android.util.Log
import com.imcys.bilibilias.base.model.login.AuthQrCodeBean
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.home.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val http: HttpClient) : BaseViewModel() {
    init {
        applyQRCode()
    }

    fun applyQRCode() {
        launchIO {
            val bean = http.get(BilibiliApi.getLoginQRPath)
                .body<AuthQrCodeBean.Data>()
            Log.d("PluginQR", "applyQRCode: $bean")
        }
    }
}
