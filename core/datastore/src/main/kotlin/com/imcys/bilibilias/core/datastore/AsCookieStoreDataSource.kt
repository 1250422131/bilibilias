package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.AsCookieStore
import com.imcys.bilibilias.core.datastore.model.Cookie
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AsCookieStoreDataSource @Inject constructor(
    private val dataStore: DataStore<AsCookieStore>,
) {
    val cookies = dataStore.data.map { it.cookies }

    suspend fun addCookie(cookie: Cookie) {
        val map = cookies.first().toMutableMap()
        map[cookie.name] = cookie
        dataStore.updateData {
            it.copy(cookies = map)
        }
    }
}
