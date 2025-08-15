package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.firstOrNull

class CookieJarDataSource(
    private val cookieDataStore: DataStore<Map<String, String>>,
) {
    val cookies = cookieDataStore.data
    suspend fun addOrUpdate(name: String, value: String) {
        cookieDataStore.updateData { currentCookies ->
            currentCookies + (name to value)
        }
    }

    suspend fun getCookie(name: String): String? {
        return cookies.firstOrNull()?.get(name)
    }

    suspend fun removeCookie(name: String) {
        cookieDataStore.updateData { currentCookies ->
            currentCookies - name
        }
    }
}