package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.model.MediaCacheSave
import com.imcys.bilibilias.core.di.applicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.builtins.ListSerializer
import org.koin.dsl.module

val DataStoreModule = module {
    single<MediaCacheDataSource> {
        DataStoreMediaCacheDataSource(
            store = DataStoreFactory.new(
                serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                produceFile = { resolveDataStoreFile("media_cache_storage") },
                scope = CoroutineScope(applicationScope.coroutineContext + Dispatchers.IO),
            )
        )
    }
}