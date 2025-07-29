package com.imcys.bilibilias.core.media.cache

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.builtins.ListSerializer
import org.koin.dsl.module

val mediaCacheModule = module {
    single<MediaCacheStorage> {
        DataStoreMediaCacheStorage(
            store = DataStoreFactory.new(
                serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                produceFile = { resolveDataStoreFile("media_cache_storage") },
                scope = CoroutineScope(AsDispatchers.applicationScope.coroutineContext + AsDispatchers.IO)
            )
        )
    }
}