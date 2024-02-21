package com.imcys.datastore.datastore

import androidx.datastore.core.*
import com.bilias.core.datastore.cookie.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

class CookieDataSource @Inject constructor(
    private val store: DataStore<CookieStorage>,
) {
    val cookies = store.data.map { it.store }
    val loginState = runBlocking {
        store.data.map { it.loginState }.first()
    }
    suspend fun setCookie(cookie: Cookie) {
        store.updateData {
            val store = it.store.toMutableMap()
            store[cookie.name] = cookie
            it.copy(store)
        }
    }

    suspend fun setLoginState(state: Boolean) {
        store.updateData {
            it.copy(loginState = state)
        }
    }
}
