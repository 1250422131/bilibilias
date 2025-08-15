package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.TokenSave
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenRepository(
    private val dataStore: DataStore<TokenSave>
) {
    val refreshToken: Flow<String?> = dataStore.data.map { it.refreshToken }
    suspend fun setRefreshToken(value: String) {
        dataStore.updateData {
            it.copy(refreshToken = value)
        }
    }
}