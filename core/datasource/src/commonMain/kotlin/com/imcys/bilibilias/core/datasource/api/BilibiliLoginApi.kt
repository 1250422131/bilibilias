package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.PollResponse
import com.imcys.bilibilias.core.datasource.model.QrCode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class BilibiliLoginApi(
    private val client: HttpClient,
) : AutoCloseable {

    suspend fun getQrcode(): QrCode {
        return client.get("/x/passport-login/web/qrcode/generate").body<QrCode>()
    }

    suspend fun pollRequest(key: String): PollResponse {
        return client.get("/x/passport-login/web/qrcode/poll") {
            parameter("qrcode_key", key)
        }.body<PollResponse>()
    }

    override fun close() {
        client.close()
    }
}