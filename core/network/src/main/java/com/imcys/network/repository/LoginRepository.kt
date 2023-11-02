package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.Result
import com.imcys.model.AuthQrCode
import com.imcys.model.LoginResponse
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.safeGet
import io.ktor.client.HttpClient
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
    suspend fun getQrCode(action: (AuthQrCode) -> Unit) {
        withContext(ioDispatcher) {
            when (val qr = httpClient.safeGet<AuthQrCode>(BilibiliApi2.getLoginQRPath)) {
                is Result.Error -> TODO()
                Result.Loading -> {}
                is Result.Success -> {
                    val url = "https://pan.misakamoe.com/qrcode/?url=${qr.data.url.encodeURLParameter()}"
                    action(AuthQrCode(qr.data.qrcodeKey, url))
                }
            }
        }
    }

    suspend fun pollLogin(key: String, action: (LoginResponse) -> Unit) {
        withContext(ioDispatcher) {
            when (val res = httpClient.safeGet<LoginResponse>(BilibiliApi2.getLoginStatePath) {
                parameter("qrcode_key", key)
            }) {
                is Result.Error -> TODO()
                Result.Loading -> TODO()
                is Result.Success -> action(res.data)
            }
        }
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
