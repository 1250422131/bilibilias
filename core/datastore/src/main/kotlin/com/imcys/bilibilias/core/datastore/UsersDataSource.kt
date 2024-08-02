package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.Users
import javax.inject.Inject

// todo 想一个更好的名字
class UsersDataSource @Inject constructor(
    private val dataStore: DataStore<Users>,
) {
    val users = dataStore.data

    suspend fun setUserId(id: Long) {
        dataStore.updateData {
            it.copy(id = id)
        }
    }

    suspend fun setLoginState(state: Boolean) {
        dataStore.updateData {
            it.copy(isLogined = state)
        }
    }

    suspend fun setMixKey(key: String) {
        dataStore.updateData {
            it.copy(mixKey = key)
        }
    }
}
