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
        logger.i { "Caching ${episodeMetadata.bvid}-${episodeMetadata.cid}" }

        store.updateData { list ->
            list + MediaCacheSave(episodeMetadata, metadata)
        }
    }

    override suspend fun delete(episodeMetadata: EpisodeMetadata): Boolean {
        var deleted = false
        val key = "${episodeMetadata.bvid}-${episodeMetadata.cid}" // For logging
        logger.d { "Attempting to delete $key" }
        store.updateData { list ->
            val originalSize = list.size
            val newList = list.filterNot { isSameEpisode(it, episodeMetadata) }
            deleted = newList.size < originalSize
            if (deleted) {
                logger.i { "Deleted $key from cache" } // Info if successful
            } else {
                logger.d { "$key not found in cache for deletion" }
            }
            newList
        }
        return deleted
    }

    private fun isSameEpisode(
        cache: MediaCacheSave,
        episodeMetadata: EpisodeMetadata
    ): Boolean {
        return cache.origin == episodeMetadata
    }


    override fun close() {

    }
}