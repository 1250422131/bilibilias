package com.imcys.bilibilias.core.datasource.persistent

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import com.imcys.bilibilias.core.datasource.persistent.TokenSave.Companion.INIT
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

object TokenPersistent {
    private val dataStore: DataStore<TokenSave> = DataStoreFactory.new(
        serializer = TokenSave.serializer().asDataStoreSerializer { INIT },
        corruptionHandler = ReplaceFileCorruptionHandler { INIT },
        produceFile = { resolveDataStoreFile("token") },
        scope = CoroutineScope(AsDispatchers.applicationScope.coroutineContext + AsDispatchers.IO)
    )

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
