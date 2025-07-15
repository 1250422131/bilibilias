package com.imcys.bilibilias.core.media.cache

import androidx.datastore.core.DataStore
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Clock

class DataStoreMediaCacheStorage(
    private val store: DataStore<List<MediaCacheSave>>,
    parentCoroutineContext: CoroutineContext = EmptyCoroutineContext,
    private val clock: Clock = Clock.System,
) : MediaCacheStorage {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override val listFlow = store.data
    private val logger = Logger.withTag("DataStoreMediaCacheStorage")

    override suspend fun cache(
        episodeMetadata: EpisodeMetadata,
        metadata: MediaCacheMetadata,
        resume: Boolean
    ) {
        logger.i { "Creating cache for ${episodeMetadata.bvid}-${episodeMetadata.cid}" }
        logger.i { "Cache metadata: $metadata" }

        store.updateData { list ->
            list + MediaCacheSave(episodeMetadata, metadata)
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


    override fun close() {

    }
}