package com.imcys.bilibilias.core.datasource.persistent

import androidx.datastore.core.DataStore
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first

internal class CookiePersistent(
    private val cookieStore: DataStore<List<Cookie>>,
) : CookiesStorage {
    val cookieFlow = cookieStore.data
    override suspend fun get(requestUrl: Url): List<Cookie> {
        return cookieFlow.first()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        setCookie(cookie)
    }
    suspend fun setCookie(cookie: Cookie) {
        cookieStore.updateData {
            it.filterNot { it.name == cookie.name }
                .plus(cookie)
        }
    }
    override fun close() {}
}