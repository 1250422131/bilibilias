package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.datastore.LoginInfoDataSource
import com.imcys.bilibilias.core.model.login.QrcodeGenerate
import com.imcys.bilibilias.core.model.login.QrcodePoll
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.WrapperClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class LoginRepository @Inject constructor(
    wrapperClient: WrapperClient,
    private val loginInfoDataSource: LoginInfoDataSource
) {
    private val client = wrapperClient.client
    suspend fun 获取二维码(): QrcodeGenerate {
        return client.get(BilibiliApi.WEB_QRCODE_GENERATE).body<QrcodeGenerate>()
    }

    suspend fun 轮询登录(key: String): QrcodePoll {
        val response = client.get(BilibiliApi.WEB_QRCODE_POLL) {
            parameter("qrcode_key", key)
        }.body<QrcodePoll>()
        val token = response.refreshToken
        if (token.isNotEmpty()) {
            loginInfoDataSource.setRefreshToken(token)
        }
        return response
    }
}