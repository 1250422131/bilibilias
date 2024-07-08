package com.imcys.bilibilias.core.network.repository

import android.content.Context
import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.model.login.Buvids
import com.imcys.bilibilias.core.model.login.NavigationBar
import com.imcys.bilibilias.core.model.login.QrcodeGenerate
import com.imcys.bilibilias.core.model.login.QrcodePoll
import com.imcys.bilibilias.core.network.api.BILIBILI_URL
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.buvidFp
import com.imcys.bilibilias.core.network.payload
import com.imcys.bilibilias.core.network.uuid2
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefalutLoginRepository @Inject constructor(
    private val client: HttpClient,
    private val loginInfoDataSource: LoginInfoDataSource,
    @ApplicationContext private val context: Context,
    private val json: Json,
) : LoginRepository {
    override suspend fun 获取二维码(): QrcodeGenerate {
        return client.get(BilibiliApi.WEB_QRCODE_GENERATE).body<QrcodeGenerate>()
    }

    override suspend fun 轮询登录(key: String): QrcodePoll {
        val response = client.get(BilibiliApi.WEB_QRCODE_POLL) {
            parameter("qrcode_key", key)
        }.body<QrcodePoll>()
        val token = response.refreshToken
        if (token.isNotEmpty()) {
            loginInfoDataSource.setRefreshToken(token)
        }
        return response
    }

    override suspend fun 导航栏用户信息(): NavigationBar {
        return client.get(BilibiliApi.NAV_BAR).body()
    }

    override suspend fun exitLogin() {
        val cookie = loginInfoDataSource.cookieStore.first()["bili_jct"]
        client.post(BilibiliApi.EXIT) {
            parameter("biliCSRF", cookie?.value_)
        }
    }

    override suspend fun getBilibiliHome() {
        client.get(BILIBILI_URL)
//        activeBuvid()
    }

    override suspend fun activeBuvid() {
        val buvids = client.get("x/frontend/finger/spi").body<Buvids>()
        loginInfoDataSource.setFinger("buvid3", buvids.b3)
        loginInfoDataSource.setFinger("buvid4", buvids.b4)

        val payload = payload(context, json)

        val res = client.post("https://api.bilibili.com/x/internal/gaia-gateway/ExClimbWuzhi") {
            contentType(ContentType.Application.Json)
            cookie("buvid3", buvids.b3)
            cookie("buvid4", buvids.b4)
            cookie("_uuid ", uuid2())
            cookie("buvid_fp  ", buvidFp(payload.inner))
            setBody(payload)
        }.bodyAsText()
        Napier.d { res }
    }
}
