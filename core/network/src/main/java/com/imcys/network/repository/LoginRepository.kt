package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.datastore.fastkv.CookiesData
import com.imcys.model.login.AuthQrCode
import com.imcys.model.login.CookieState
import com.imcys.model.login.LoginResponse
import com.imcys.model.login.NewRefreshToken
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.parameterCSRF
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.contentType
import io.ktor.http.encodeURLParameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@Singleton
class LoginRepository @Inject constructor(
    private val httpClient: HttpClient,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val cookiesData: CookiesData
) {
    suspend fun getQRCode(): AuthQrCode = withContext(ioDispatcher) {
        val authQrCode = httpClient.get(BilibiliApi2.getLoginQRPath).body<AuthQrCode>()
        authQrCode.copy(
            authQrCode.qrcodeKey,
            "https://pan.misakamoe.com/qrcode/?url=${authQrCode.url.encodeURLParameter()}"
        )
    }

    suspend fun pollLogin(key: String): LoginResponse = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi2.getLoginStatePath) {
            parameter("qrcode_key", key)
        }.body()
    }

    /**
     * curl -L -X POST 'https://passport.bilibili.com/login/exit/v2' \
     * -H 'Cookie: DedeUserID=xxx; bili_jct=xxx; SESSDATA=xxx' \
     * -H 'Content-Type: application/x-www-form-urlencoded' \
     * --data-urlencode 'biliCSRF=xxxxxx'
     * ```
     * val formBody = FormBody.Builder()
     *   .add("biliCSRF", "xxxxxx")
     *   .build()
     * val request = Request.Builder()
     *   .url("https://passport.bilibili.com/login/exit/v2")
     *   .post(formBody)
     *   .header("Cookie", "DedeUserID=xxx; bili_jct=xxx; SESSDATA=xxx")
     *   .header("Content-Type", "application/x-www-form-urlencoded")
     *   .build()
     * ```
     */
    suspend fun logout(): Unit = withContext(ioDispatcher) {
        httpClient.post(BilibiliApi2.exitLogin) {
            header(
                "Cookie",
                "DedeUserID=${cookiesData.userID};" +
                        "bili_jct=${cookiesData.csrf};" +
                        "SESSDATA=${cookiesData.sessionData}"
            )
            contentType(ContentType.Application.FormUrlEncoded)
            parameter("biliCSRF", cookiesData.csrf)
        }
    }

    /**
     * curl -G 'https://passport.bilibili.com/x/passport-login/web/cookie/info' \
     * 	--data-urlencode 'csrf=xxx' \
     * 	-b 'SESSDATA=xxx'
     * ```
     * val request = Request.Builder()
     *   .url("https://passport.bilibili.com/x/passport-login/web/cookie/info?csrf=xxx")
     *   .header("Cookie", "SESSDATA=xxx")
     *   .build()
     * ```
     */
    suspend fun checkCookieNeedRefresh(): CookieState = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi2.CHECK_COOKIE_REFRESH) {
            contentType(ContentType.Application.FormUrlEncoded)
            parameterCSRF(cookiesData.csrf)
            headerCookie(cookiesData.sessionData)
        }.body()
    }

    suspend fun getRefreshCsrf(correspondPath: String): String = withContext(ioDispatcher) {
        val html = httpClient.get(BilibiliApi2.CORRESPOND + correspondPath) {
            headerCookie(cookiesData.sessionData)
        }.bodyAsText()
        if (html.isEmpty()) return@withContext ""
        val target = "<div id=\"1-name\">"
        val index = html.indexOf(target) + target.length
        var str = ""
        for (i in index..<html.length) {
            val c = html[i]
            if (c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9') {
                str += c
            } else break
        }

        str
    }

    /**
     * ```
     * val formBody = FormBody.Builder()
     *   .add("csrf", "f610640a37f51f6266f6b83cfc5eedbb")
     *   .add("refresh_csrf", "b0cc8411ded2f9db2cff2edb3123acac")
     *   .add("source", "main_web")
     *   .add("refresh_token", "45240a041836905fe953e3b98b83d751")
     *   .build()
     *
     * val request = Request.Builder()
     *   .url("https://passport.bilibili.com/x/passport-login/web/cookie/refresh")
     *   .post(formBody)
     *   .header("Cookie", "SESSDATA=xxx")
     *   .header("Content-Type", "application/x-www-form-urlencoded")
     *   .build()
     * ```
     */
    suspend fun refreshCookie(refreshCsrf: String): String = withContext(ioDispatcher) {
        val oldRefreshToken = cookiesData.refreshToken
        val newRefreshToken = httpClient.post(BilibiliApi2.COOKIE_REFRESH) {
            contentType(ContentType.Application.FormUrlEncoded)
            headerCookie(cookiesData.sessionData)
            parameterCSRF(cookiesData.csrf)
            parameter("refresh_csrf", refreshCsrf)
            parameter("source", "main_web")
            parameter("refresh_token", oldRefreshToken)
        }.body<NewRefreshToken>()
        cookiesData.refreshToken = newRefreshToken.refreshToken
        oldRefreshToken
    }

    private fun HttpMessageBuilder.headerCookie(sessionData: String): Unit =
        headers.append("Cookie", "SESSDATA=${sessionData}")

    /**
     * ```
     * val formBody = FormBody.Builder()
     *   .add("csrf", "1e9658858e6da76be64bd92cdc0fa324")
     *   .add("refresh_token", "45240a041836905fe953e3b98b83d751")
     *   .build()
     *
     * val request = Request.Builder()
     *   .url("https://passport.bilibili.com/x/passport-login/web/confirm/refresh")
     *   .post(formBody)
     *   .header("Cookie", "SESSDATA=xxx")
     *   .header("Content-Type", "application/x-www-form-urlencoded")
     *   .build()
     * ```
     */
    suspend fun confirmRefresh(oldRefreshToken: String): Unit = withContext(ioDispatcher) {
        httpClient.post(BilibiliApi2.CONFIRM_REFRESH) {
            contentType(ContentType.Application.FormUrlEncoded)
            headerCookie(cookiesData.sessionData)
            parameterCSRF(cookiesData.csrf)
            parameter("refresh_token", oldRefreshToken)
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getCorrespondPath(timestamp: Long): String {
        val publicKeyPEM = """
        -----BEGIN PUBLIC KEY-----
        MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg
        Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71
        nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40
        JNrRuoEUXpabUzGB8QIDAQAB
        -----END PUBLIC KEY-----
    """.trimIndent()
        return try {
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
                    OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT)
                )
            }
            cipher.doFinal("refresh_$timestamp".toByteArray()).joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e)
            ""
        } catch (e: NoSuchPaddingException) {
            Timber.e(e)
            ""
        } catch (e: InvalidKeySpecException) {
            Timber.e(e)
            ""
        } catch (e: java.security.InvalidAlgorithmParameterException) {
            Timber.e(e)
            ""
        } catch (e: java.security.InvalidKeyException) {
            Timber.e(e)
            ""
        } catch (e: javax.crypto.BadPaddingException) {
            Timber.e(e)
            ""
        } catch (e: javax.crypto.IllegalBlockSizeException) {
            Timber.e(e)
            ""
        }
    }
}
