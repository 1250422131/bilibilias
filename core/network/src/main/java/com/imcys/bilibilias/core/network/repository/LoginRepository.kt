package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.login.QrcodeGenerate
import com.imcys.bilibilias.core.model.login.QrcodePoll
import com.imcys.bilibilias.core.network.api.BilibiliApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class LoginRepository @Inject constructor(private val client: HttpClient) {
    suspend fun 获取二维码(): QrcodeGenerate {
        return client.get(BilibiliApi.WEB_QRCODE_GENERATE).body<QrcodeGenerate>()
    }

    suspend fun 轮询登录接口(key: String): QrcodePoll {
        return client.get(BilibiliApi.WEB_QRCODE_POLL) {
            parameter("qrcode_key", key)
        }.body<QrcodePoll>()
    }
}