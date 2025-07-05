package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.config.AsCookie
import com.imcys.bilibilias.common.base.config.UserInfoRepository
import com.imcys.bilibilias.common.base.config.toAsCookie
import com.imcys.bilibilias.common.base.config.toCookie
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Singleton
class AsCookiesStorage @Inject constructor(
        private val json: Json,
        private val cbor: Cbor
) : CookiesStorage {
    private val cookies = mutableListOf<Cookie>()

    init {
        if (UserInfoRepository.asCookies.isNotEmpty()) {
            cbor.decodeFromByteArray<List<AsCookie>>(UserInfoRepository.asCookies)
                    .map { it.toCookie() }
                    .forEach {
                        cookies.add(it)
                    }


        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
//        val cookie = cookies.find { it.name == "SESSDATA" } ?: return emptyList()
        return cookies
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val timestamp = cookie.expires?.timestamp ?: 0
        if (timestamp < System.currentTimeMillis()) return
        cookies.add(cookie)
    }

    fun addCookieByUrl(url: Url, key: String, value: String) {
        val existingCookie = cookies.find { it.name == key && it.domain == url.host }

        // 如果存在，则移除旧的 Cookie
        if (existingCookie != null) {
            cookies.remove(existingCookie)
        }
        val cookie = Cookie(
            name = key,
            value = value,
            domain = url.host, // 假设 cookie 的域名与 URL 的域名一致
            path = "/", // 假设路径为根路径
            expires = null, // 不设置过期时间
            maxAge = null,
            httpOnly = true, // 假设是 HttpOnly 的 cookie
            secure = true, // 如果 URL 是 HTTPS，则设置为 Secure
        )
        cookies.add(cookie)
    }


    fun saveCookies() {
        UserInfoRepository.asCookies = cbor.encodeToByteArray(cookies.map { it.toAsCookie() })
    }

    fun getCookieValue(key: String): String? {
        return cookies.find { it.name == key }?.value
    }

    fun getAllCookies(): String {
        return cookies.joinToString(";") {
            "${it.name}=${it.value}"
        }
    }

    fun deleteAllCookie(){
        UserInfoRepository.clearAllKV()
    }

    override fun close() {
        UserInfoRepository.asCookies = cbor.encodeToByteArray(cookies.map { it.toAsCookie() })
    }
}
