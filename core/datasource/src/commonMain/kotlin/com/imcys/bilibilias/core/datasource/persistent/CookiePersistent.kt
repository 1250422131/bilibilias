package com.imcys.bilibilias.core.datasource.persistent

import androidx.datastore.core.DataStore
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal class CookiePersistent : CookiesStorage, KoinComponent {
    private val cookieStore: DataStore<List<Cookie>> by inject(named("Cookie"))
    val cookieFlow = cookieStore.data
    override suspend fun get(requestUrl: Url): List<Cookie> {
        return cookieFlow.first()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        setCookie(cookie)
    }

    suspend fun setCookie(cookie: Cookie) {
        cookieStore.updateData { store ->
            store.filterNot { it.name == cookie.name }
                .plus(cookie)
        }
    }

    override fun close() {}
}