package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.login.AuthQrCode
import com.imcys.model.login.LoginResponse
import com.imcys.network.api.BilibiliApi2
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val httpClient: HttpClient,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getQRCode(): AuthQrCode = withContext(ioDispatcher) {
        val authQrCode = httpClient.get(BilibiliApi2.getLoginQRPath).body<AuthQrCode>()
        authQrCode.copy(
            authQrCode.qrcodeKey,
            "https://pan.misakamoe.com/qrcode/?url=${authQrCode.url.encodeURLParameter()}"
        )
    }

    suspend fun pollLogin(key: String): LoginResponse = withContext(ioDispatcher){
       httpClient.get(BilibiliApi2.getLoginStatePath) {
            parameter("qrcode_key", key)
        }.body()
    }

    suspend fun logout(cookie: String, jct: String, userID: String): Unit = withContext(ioDispatcher) {
        httpClient.post(BilibiliApi2.exitLogin) {
            headers {
                append(
                    "Cookie",
                    "DedeUserID=${userID};" +
                            "bili_jct=${jct};" +
                            "SESSDATA=${cookie}"
                )
            }
            contentType(ContentType.Application.FormUrlEncoded)
            parameter("biliCSRF", jct)
        }
    }
}
