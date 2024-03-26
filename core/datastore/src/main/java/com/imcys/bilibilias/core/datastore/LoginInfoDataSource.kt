package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.bilias.core.datastore.cookie.Cookie
import com.bilias.core.datastore.cookie.LoginInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginInfoDataSource @Inject constructor(
    private val loginInfo: DataStore<LoginInfo>,
) {
    val cookieStore = loginInfo.data.map { it.store }
    val loginState = loginInfo.data.map { it.loginState }
    val refreshToken = loginInfo.data.map { it.refreshToken }
    val mixKey = loginInfo.data.map { it.mixKey }

    suspend fun setCookie(cookie: Cookie) {
        val map = cookieStore.map { it.toMutableMap() }.first()
        map[cookie.name] = cookie
        loginInfo.updateData { info ->
            info.copy(map)
        }
    }

    suspend fun setLoginState(state: Boolean) {
        loginInfo.updateData {
            it.copy(loginState = state)
        }
    }

    suspend fun setRefreshToken(token: String) {
        loginInfo.updateData {
            it.copy(refreshToken = token)
        }
    }

    suspend fun setMixKey(key: String) {
        loginInfo.updateData {
            it.copy(mixKey = key)
        }
    }
}
