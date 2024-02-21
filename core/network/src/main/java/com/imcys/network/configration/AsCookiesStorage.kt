package com.imcys.network.configration

import com.imcys.datastore.datastore.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*
import com.bilias.core.datastore.cookie.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie


class AsCookiesStorage @Inject constructor(
    private val cookieDataSource: CookieDataSource
) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        Timber.d("addCookie: $cookie")
        cookieDataSource.setCookie(cookie.mapToAsCookie())
        cookieDataSource.setLoginState(true)
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> {
        Timber.d("getCookie: $requestUrl")
        return cookieDataSource.cookies.first().values.map(AsCookie::mapToKtorCookie)
    }

    override fun close() = Unit
}

internal fun AsCookie.mapToKtorCookie(): KtorCookie = KtorCookie(
    name,
    value_,
    CookieEncoding.RAW,
    maxAge,
    GMTDate(timestamp),
    domain, path, secure, httpOnly
)

internal fun KtorCookie.mapToAsCookie(): AsCookie = AsCookie(
    name,
    value,
    maxAge ?: 0,
    expires?.timestamp ?: 0,
    domain ?: "",
    path ?: "",
    secure,
    httpOnly
)
