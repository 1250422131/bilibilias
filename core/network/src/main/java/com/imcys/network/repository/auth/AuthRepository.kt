package com.imcys.network.repository.auth

import com.imcys.datastore.fastkv.CookieStorage
import com.imcys.model.login.AuthQrCode
import com.imcys.model.login.CookieInfo
import com.imcys.model.login.CookieRefresh
import com.imcys.model.login.LoginResponse
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.parameterCSRF
import com.imcys.network.utils.parameterRefreshCsrf
import com.imcys.network.utils.parameterRefreshToken
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import java.security.KeyFactory
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Singleton
class AuthRepository @Inject constructor(
    private val client: HttpClient,
    private val cookieStorage: CookieStorage
) : IAuthDataSources {

    @OptIn(ExperimentalEncodingApi::class)
    private fun getCorrespondPath(timestamp: Long): String {
        val publicKeyPEM = """
        -----BEGIN PUBLIC KEY-----
        MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg
        Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71
        nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40
        JNrRuoEUXpabUzGB8QIDAQAB
        -----END PUBLIC KEY-----
        """.trimIndent()
        val publicKey = KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(
                Base64.decode(
                    publicKeyPEM
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replace("\n", "")
                        .trim()
                )
            )
        )
        val cipher = Cipher.getInstance("RSA/ECB/OAEPPadding").apply {
            init(
                Cipher.ENCRYPT_MODE,
                publicKey,
                OAEPParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT
                )
            )
        }
        return cipher.doFinal("refresh_$timestamp".toByteArray())
            .joinToString("") { "%02x".format(it) }
    }

    override suspend fun 获取二维码(): AuthQrCode =
        client.get(BilibiliApi2.getLoginQRPath).body<AuthQrCode>()

    override suspend fun 轮询登录接口(key: String): LoginResponse =
        client.get(BilibiliApi2.getLoginStatePath) {
            parameter("qrcode_key", key)
        }.body()

    override suspend fun 退出登录() {
        cookieStorage.clear()
    }

    override suspend fun 检查Cookie是否需要刷新(): CookieInfo {
        val info = client.get("https://passport.bilibili.com/x/passport-login/web/cookie/info")
            .body<CookieInfo>()
        Napier.d(tag = "cookie刷新第一步") { "刷新: ${info.refresh}\n时间戳: ${info.timestamp}" }
        return info
    }

    override suspend fun 获取RefreshCsrf(timestamp: Long): String {
        val path = getCorrespondPath(timestamp)
        val text =
            client.get("https://www.bilibili.com/correspond/1/$path").bodyAsText()
        val reCsrf = fromHtmlGetRefreshCsrfBy(text)
        Napier.d(tag = "cookie刷新第二步") { "Correspond: $path\nRefreshCsrf: $reCsrf" }
        return reCsrf
    }

    private fun fromHtmlGetRefreshCsrfBy(html: String): String {
        val regex = Regex("<div +id=\"1-name\">(.+?)</div>")
        val refreshCsrf = regex.findAll(html).map { it.value }.firstOrNull() ?: ""
        return refreshCsrf.drop("<div id=\"1-name\">".length).dropLast("</div>".length)
    }

    override suspend fun 刷新Cookie(
        csrf: String,
        refresh_csrf: String,
        refresh_token: String
    ): CookieRefresh {
        val cookieRefresh =
            client.post("https://passport.bilibili.com/x/passport-login/web/cookie/refresh") {
                parameterCSRF(refresh_csrf)
                parameterRefreshCsrf(refresh_csrf)
                parameterRefreshToken(refresh_token)
                parameter("source", "main_web")
            }
                .body<CookieRefresh>()
        Napier.d(tag = "cookie刷新第三步") { "刷新Token: ${cookieRefresh.refreshToken}" }
        return cookieRefresh
    }

    override suspend fun 确认更新Cookie(csrf: String, refreshToken: String) {
        val text =
            client.post("https://passport.bilibili.com/x/passport-login/web/confirm/refresh") {
                parameterCSRF(csrf)
                parameterRefreshToken(refreshToken)
            }
                .bodyAsText()
        Napier.d(tag = "cookie刷新最后一步") { text }
    }

    suspend fun cookieRefreshChain() {
        val (needRefresh, timestamp) = 检查Cookie是否需要刷新()
        if (!needRefresh) return
        val refreshCsrf = 获取RefreshCsrf(timestamp)
        val oldToken = cookieStorage.refreshToken
        val (_, newRefreshToken, _) = 刷新Cookie(
            csrf = cookieStorage.biliJctOrCsrf,
            refresh_csrf = refreshCsrf,
            refresh_token = oldToken
        )
        确认更新Cookie(cookieStorage.biliJctOrCsrf, oldToken)
        cookieStorage.setRefreshToke(newRefreshToken)
    }
}
