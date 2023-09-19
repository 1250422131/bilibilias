package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.AuthQrCodeBean
import com.imcys.bilibilias.common.base.model.LoginResponseBean
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val httpClient: HttpClient) {
    suspend fun applyForQrCode(action: (AuthQrCodeBean.Data) -> Unit) =
        httpClient.safeGet<AuthQrCodeBean.Data>(BilibiliApi.getLoginQRPath)
            .onSuccess(action)

    suspend fun pollingLogin(key: String) =
        httpClient.safeGet<LoginResponseBean>(BilibiliApi.getLoginStatePath) {
            parameter("qrcode_key", key)
        }
}
