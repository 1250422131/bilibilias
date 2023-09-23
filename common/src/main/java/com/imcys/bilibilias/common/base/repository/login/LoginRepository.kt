package com.imcys.bilibilias.common.base.repository.login

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.repository.login.model.AuthQrCode
import com.imcys.bilibilias.common.base.repository.login.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.encodeURLParameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val httpClient: HttpClient) {
    suspend fun getQrCode(action: (AuthQrCode) -> Unit) {
        httpClient.safeGet<AuthQrCode>(BilibiliApi.getLoginQRPath).onSuccess {
            val url = "https://pan.misakamoe.com/qrcode/?url=${it.url.encodeURLParameter()}"
            action(AuthQrCode(it.qrcodeKey, url))
        }
    }

    suspend fun onPollingLogin(key: String, action: (LoginResponse) -> Unit) =
        httpClient.safeGet<LoginResponse>(BilibiliApi.getLoginStatePath) {
            parameter("qrcode_key", key)
        }.onSuccess(action)
}
