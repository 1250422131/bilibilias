package com.imcys.bilibilias.core.network.ktor

import com.imcys.bilibilias.core.datastore.AsCookieStoreDataSource
import com.imcys.bilibilias.core.datastore.UsersDataSource
import io.github.aakira.napier.Napier
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.imcys.bilibilias.core.datastore.model.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie

class AsCookiesStorage @Inject constructor(
    private val asCookieStoreDataSource: AsCookieStoreDataSource,
    private val usersDataSource: UsersDataSource,
) : CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        Napier.d(tag = "CookiesStorage") { cookie.toString() }
        asCookieStoreDataSource.addCookie(cookie.mapToAsCookie())
        if (cookie.name == "SESSDATA") usersDataSource.setLoginState(true)
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> = asCookieStoreDataSource.cookies.first().values.map(AsCookie::mapToKtorCookie)

    override fun close() = Unit
}

private fun AsCookie.mapToKtorCookie() =
    KtorCookie(
        name,
        value,
        CookieEncoding.RAW,
        maxAge,
        GMTDate(timestamp),
        domain,
        path,
        secure,
        httpOnly,
    )

private fun KtorCookie.mapToAsCookie() = AsCookie(
    name,
    value,
    maxAge,
    expires?.timestamp,
    domain,
    path,
    secure,
    httpOnly,
)
