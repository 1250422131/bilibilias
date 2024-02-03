package com.imcys.network.configration

import com.imcys.datastore.fastkv.CookieStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import com.imcys.model.login.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie

@Singleton
class CookieManager
@Inject constructor(
    private val cookieStorage: CookieStorage
) : CookiesStorage {

    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        Timber.d("addCookie: $cookie")
        cookieStorage.setCookie(cookie.mapToAsCookie())
        close()
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> {
        Timber.d("getCookie: $requestUrl")
        return cookieStorage.getCookie().map(AsCookie::mapToKtorCookie)
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
