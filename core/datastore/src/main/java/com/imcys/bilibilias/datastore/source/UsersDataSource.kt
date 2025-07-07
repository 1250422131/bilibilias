package com.imcys.bilibilias.datastore.source

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.datastore.User
import com.imcys.bilibilias.datastore.copy
import kotlinx.coroutines.flow.first

class UsersDataSource(
    private val dataStore: DataStore<User>,
) {
    val users = dataStore.data

    suspend fun setUserId(id: Long) {
        dataStore.updateData {
            it.copy {
                currentUserId = id
            }
        }
    }

    suspend fun getUserId(): Long {
        return dataStore.data.first().currentUserId
    }

    suspend fun isLogin() = getUserId() != 0L

}