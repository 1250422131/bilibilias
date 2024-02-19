package com.imcys.network.configration

import com.imcys.datastore.fastkv.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import timber.log.*
import com.imcys.model.login.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie


object CookieManager : CookiesStorage {
    private val cache = mutableListOf<KtorCookie>()

    init {
        if (PersistentCookie.logging) {
            PersistentCookie.getCookie().map(AsCookie::mapToKtorCookie).forEach(cache::add)
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        Timber.d("addCookie: $cookie")
        cache.findLast { it.name == cookie.name }?.let {
            cache.remove(cookie)
        }
        cache += cookie
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> {
        Timber.d("getCookie: $requestUrl")
        return cache
    }

    fun save() {
        PersistentCookie.setCookie(cache.map(KtorCookie::mapToAsCookie))
    }

    fun getCookie(): List<AsCookie> {
        return cache.map(KtorCookie::mapToAsCookie)
    }

    override fun close() = Unit
}

internal fun AsCookie.mapToKtorCookie(): KtorCookie = KtorCookie(
    name,
    value,
    CookieEncoding.RAW,
    maxAge,
    GMTDate(timestamp),
    domain, path, secure, httpOnly
)

internal fun KtorCookie.mapToAsCookie(): AsCookie = AsCookie(
    name,
    value,
    maxAge,
    expires?.timestamp ?: 0,
    domain,
    path,
    secure,
    httpOnly
)
