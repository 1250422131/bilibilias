package com.imcys.bilibilias.core.datasource.persistent

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class TokenPersistent : KoinComponent {
    private val dataStore: DataStore<TokenSave> by inject(named("TokenSave"))
    val refreshToken: Flow<String?> = dataStore.data.map { it.refreshToken }
    suspend fun setRefreshToken(value: String) {
        dataStore.updateData {
            it.copy(refreshToken = value)
        }
    }
}

@ConsistentCopyVisibility
@Serializable
data class TokenSave internal constructor(
    val refreshToken: String? = null,
) {
    companion object {
        val INIT = TokenSave()
    }
}
