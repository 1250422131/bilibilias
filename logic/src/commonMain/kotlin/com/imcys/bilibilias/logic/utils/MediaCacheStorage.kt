package com.imcys.bilibilias.logic.utils

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.media.cache.DataStoreMediaCacheStorage
import com.imcys.bilibilias.core.media.cache.MediaCacheSave
import com.imcys.bilibilias.core.media.cache.MediaCacheStorage
import kotlinx.serialization.builtins.ListSerializer
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun createDataStoreMediaCacheStorage(): MediaCacheStorage {
    return DataStoreMediaCacheStorage(
        store = DataStoreFactory.new(
            serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
            produceFile = { resolveDataStoreFile("media_cache_storage") },
        )
    )
}