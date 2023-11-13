package com.imcys.bilibilias.common.di

import com.imcys.bilibilias.common.base.config.AsCookie
import com.imcys.bilibilias.common.base.config.UserInfoRepository
import com.imcys.bilibilias.common.base.config.toAsCookie
import com.imcys.bilibilias.common.base.config.toCookie
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsCookiesStorage @Inject constructor(
    private val json: Json
) : CookiesStorage {
    private val cookies = mutableListOf<Cookie>()

    init {
        UserInfoRepository.asCookies?.map {
            json.decodeFromString<AsCookie>(it)
        }
            ?.map { it.toCookie() }
            ?.map { cookies.add(it) }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        val cookie = cookies.find { it.name == "SESSDATA" } ?: return emptyList()
        return listOf(cookie)
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        val timestamp = cookie.expires?.timestamp ?: 0
        if (timestamp < System.currentTimeMillis()) return
        cookies.add(cookie)
        UserInfoRepository.asCookies?.add(json.encodeToString(cookie.toAsCookie()))
    }

    override fun close() = Unit
}
