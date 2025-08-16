package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.PollResponse
import com.imcys.bilibilias.core.datasource.model.QrCode
import com.imcys.bilibilias.core.datasource.model.UserProfile
import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf

class BilibiliLoginApi(
    private val client: HttpClient,
    private val cookieJar: CookieJarDataSource,
) : AutoCloseable {

    suspend fun getQrcode(): QrCode {
        return client.get("/x/passport-login/web/qrcode/generate").body<QrCode>()
    }

    suspend fun pollRequest(key: String): PollResponse {
        return client.get("/x/passport-login/web/qrcode/poll") {
            parameter("qrcode_key", key)
        }.body<PollResponse>()
    }
    suspend fun getUserProfile(): UserProfile {
        return client.get("member/web/account").body<UserProfile>()
    }
    suspend fun exit() {
        val csrf = cookieJar.getCookie("bili_jct") ?: return
        client.post("/login/exit/v2") {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                FormDataContent(
                    parametersOf("biliCSRF", csrf)
                )
            )
        }
    }

    override fun close() {
        client.close()
    }
}