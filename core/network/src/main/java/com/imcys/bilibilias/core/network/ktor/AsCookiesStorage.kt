package com.imcys.bilibilias.core.network.ktor

import com.imcys.bilibilias.core.datastore.login.LoginInfoDataSource
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.CookieEncoding
import io.ktor.http.Url
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.imcys.bilibilias.core.datastore.Cookie as AsCookie
import io.ktor.http.Cookie as KtorCookie

class AsCookiesStorage @Inject constructor(private val loginInfoDataSource: LoginInfoDataSource) :
    CookiesStorage {
    override suspend fun addCookie(requestUrl: Url, cookie: KtorCookie) {
        loginInfoDataSource.setCookie(cookie.mapToAsCookie())
        if (cookie.name == "SESSDATA") loginInfoDataSource.setLoginState(true)
    }

    override suspend fun get(requestUrl: Url): List<KtorCookie> {
        return loginInfoDataSource.cookieStore.first().values.map(AsCookie::mapToKtorCookie)
    }

    suspend fun getAllCookies(): String {
        return loginInfoDataSource.cookieStore.first().values.map(AsCookie::mapToKtorCookie)
            .toString()
    }

    override fun close() = Unit
}

private fun AsCookie.mapToKtorCookie() = KtorCookie(name, value_, CookieEncoding.RAW)

private fun KtorCookie.mapToAsCookie() = AsCookie(name, value)
