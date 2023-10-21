package com.imcys.bilibilias.common.base.repository.login

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.config.CookieRepository
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.repository.login.model.AuthQrCode
import com.imcys.bilibilias.common.base.repository.login.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val httpClient: HttpClient) {
    // todo userRepository
    suspend fun getQrCode(action: (AuthQrCode) -> Unit) {
        when (val qr = httpClient.safeGet<AuthQrCode>(BilibiliApi.getLoginQRPath)) {
            is Result.Error -> TODO()
            Result.Loading -> {}
            is Result.Success -> {
                val url = "https://pan.misakamoe.com/qrcode/?url=${qr.data.url.encodeURLParameter()}"
                action(AuthQrCode(qr.data.qrcodeKey, url))
            }
        }
    }

    suspend fun onPollingLogin(key: String, action: (LoginResponse) -> Unit) {
        when (val res = httpClient.safeGet<LoginResponse>(BilibiliApi.getLoginStatePath) {
            parameter("qrcode_key", key)
        }) {
            is Result.Error -> TODO()
            Result.Loading -> TODO()
            is Result.Success -> action(res.data)
        }
    }

    suspend fun logout() = httpClient.post(BilibiliApi.exitLogin) {
        headers {
            append(
                "Cookie",
                "DedeUserID=${CookieRepository.DedeUserID};" +
                        "bili_jct=${CookieRepository.bili_jct};" +
                        "SESSDATA=${CookieRepository.sessionData}"
            )
        }
        contentType(ContentType.Application.FormUrlEncoded)
        parameter("biliCSRF", CookieRepository.bili_jct)
    }
}
