package com.imcys.bilibilias.core.datasource.persistent

import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import io.ktor.http.parseServerSetCookieHeader
import io.ktor.http.renderSetCookieHeader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class CookiesStorageImpl(
    private val cookieJar: CookieJarDataSource,
) : CookiesStorage {
    override suspend fun get(requestUrl: Url): List<Cookie> {
        return cookieJar.cookies.map {
            it.map { parseServerSetCookieHeader(it) }
        }.first()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        cookieJar.add(cookie.name, renderSetCookieHeader(cookie))
    }

    override fun close() = Unit
}