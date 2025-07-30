package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.PollResponse
import com.imcys.bilibilias.core.datasource.model.QrCode
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object BilibiliLoginApi : AutoCloseable, KoinComponent {
    private val client = createHttpClient {
        defaultRequest {
            url("https://passport.bilibili.com")
        }
        install(ApiResponseUnwrapper)
        install(ContentNegotiation) {
            json(HttpClientJson)
        }
        install(HttpCookies) {
            storage = get<CookiesStorage>()
        }
        BrowserUserAgent()
        Logging {
            level = LogLevel.BODY
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i("BilibiliLoginApi") { message }
                }
            }
        }
    }

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