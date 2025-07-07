package com.imcys.bilibilias.core.datasource.persistent

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first

internal object CookiesStorageImpl : CookiesStorage {
    override suspend fun get(requestUrl: Url): List<Cookie> {
        return CookiePersistent.cookieFlow.first()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        CookiePersistent.setCookie(cookie)
    }

    override fun close() {}
}