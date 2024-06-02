package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import com.imcys.bilibilias.core.model.login.NavigationBar
import com.imcys.bilibilias.core.model.login.QrcodeGenerate
import com.imcys.bilibilias.core.model.login.QrcodePoll
import com.imcys.bilibilias.core.network.api.BILIBILI_URL
import com.imcys.bilibilias.core.network.api.BilibiliApi

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val client: HttpClient,
    private val loginInfoDataSource: LoginInfoDataSource
) {
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

    suspend fun 导航栏用户信息(): NavigationBar {
        return client.get(BilibiliApi.NAV_BAR).body()
    }

    suspend fun exitLogin() {
        val cookie = loginInfoDataSource.cookieStore.first()["bili_jct"]
        client.post(BilibiliApi.EXIT) {
            parameter("biliCSRF", cookie?.value_)
        }
    }

    suspend fun getBilibiliHome(): Unit {
        client.get(BILIBILI_URL)
    }
}
