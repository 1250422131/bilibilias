package com.imcys.network.configration

import com.imcys.datastore.fastkv.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import timber.log.*
import javax.inject.*
import com.imcys.model.login.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie

@Singleton
class CookieManager @Inject constructor(
    private val persistentCookie: PersistentCookie
) : CookiesStorage {

    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        Timber.d("addCookie: $cookie")
        persistentCookie.setCookie(cookie.mapToAsCookie())
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> {
        Timber.d("getCookie: $requestUrl")
        return persistentCookie.getCookie().map(AsCookie::mapToKtorCookie)
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
