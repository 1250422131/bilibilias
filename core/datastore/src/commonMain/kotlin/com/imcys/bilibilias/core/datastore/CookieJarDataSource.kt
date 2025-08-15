package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CookieJarDataSource(
    private val cookieDataStore: DataStore<Map<String, String>>,
) {
    val cookies = cookieDataStore.data.map { it.values.toList() }
    suspend fun add(name: String, value: String) {
        cookieDataStore.updateData { currentCookies ->
            currentCookies + (name to value)
        }
    }

    suspend fun getCookie(name: String): String? {
        return cookieDataStore.data.first()[name]
    }

    suspend fun removeCookie(name: String) {
        cookieDataStore.updateData { currentCookies ->
            currentCookies - name
        }
    }
    suspend fun clearCookies() {
        cookieDataStore.updateData { emptyMap() }
    }
}