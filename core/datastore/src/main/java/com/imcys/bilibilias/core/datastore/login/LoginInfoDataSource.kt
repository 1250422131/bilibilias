package com.imcys.bilibilias.core.datastore.login

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.Cookie
import com.imcys.bilibilias.core.datastore.LoginInfo
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
    val mid = loginInfo.data.map { it.mid }
    val finger = loginInfo.data.map { it.finger }
    suspend fun setMid(mid: Long) {
        loginInfo.updateData {
            it.copy(mid = mid)
        }
    }

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

    suspend fun setFinger(key: String, value: String) {
        loginInfo.updateData {
            val new = it.finger.toMutableMap()
            new[key] = value
            it.copy(finger = new)
        }
    }
}
