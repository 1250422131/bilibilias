package com.imcys.bilibilias.core.media.cache

import androidx.datastore.core.DataStore
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Clock

class DataStoreMediaCacheStorage(
    private val store: DataStore<List<MediaCacheSave>>,
    parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val clock: Clock = Clock.System,
) : MediaCacheStorage {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val listFlow = MutableStateFlow<List<MediaCache>>(emptyList())
    private val logger = Logger.withTag("DataStoreMediaCacheStorage")
    private val lock = Mutex()
    override suspend fun cache(
        episodeMetadata: EpisodeMetadata,
        metadata: MediaCacheMetadata,
        resume: Boolean
    ): MediaCache {
        logger.i { "Creating cache for ${episodeMetadata.bvid}-${episodeMetadata.cid}" }
        logger.i { "Cache metadata: $metadata" }
        return lock.withLock {
            listFlow.value.firstOrNull {
                isSameMediaAndEpisode(it, episodeMetadata)
            }?.let { return@withLock it }

            val cache = MediaCache(episodeMetadata, metadata)
            store.updateData { list ->
                list + MediaCacheSave(episodeMetadata, metadata)
            }

            listFlow.update { it + cache }
            cache
        }
    }

    private fun isSameMediaAndEpisode(
        cache: MediaCacheSave,
        episodeMetadata: EpisodeMetadata
    ): Boolean {
        return cache.origin == episodeMetadata
    }

    private fun isSameMediaAndEpisode(
        cache: MediaCache,
        episodeMetadata: EpisodeMetadata
    ): Boolean {
        return cache.origin == episodeMetadata
    }

    private suspend fun refreshCache(): List<MediaCache> {
        return lock.withLock {
            val allRecovered = MutableStateFlow(listOf<MediaCache>())
            val semaphore = Semaphore(8)
            emptyList()
        }
    }


    override fun close() {

    }
}