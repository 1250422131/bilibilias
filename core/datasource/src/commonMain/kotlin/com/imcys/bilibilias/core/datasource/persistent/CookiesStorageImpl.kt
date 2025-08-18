package com.imcys.bilibilias.core.datasource.persistent

import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class CookiesStorageImpl(
    private val cookieJar: CookieJarDataSource,
) : CookiesStorage {
    override suspend fun get(requestUrl: Url): List<Cookie> {
        return cookieJar.cookies.map {
            it.map { Cookie(it.key, it.value) }
        }.first()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        cookieJar.add(cookie.name, cookie.value)
    }

    override fun close() = Unit
}