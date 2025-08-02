package com.imcys.bilibilias.core.datastore

import androidx.datastore.core.DataStore
import com.imcys.bilibilias.core.datastore.model.EpisodeMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCacheMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCachePartMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCacheSave
import com.imcys.bilibilias.core.logging.logger
import kotlin.time.Clock

// TODO: 及时更新时间
internal class DataStoreMediaCacheDataSource(
    private val store: DataStore<List<MediaCacheSave>>,
    private val clock: Clock = Clock.System,
) : MediaCacheDataSource {

    override val listFlow = store.data

    override suspend fun cacheEpisodeMetadata(episodeMetadata: EpisodeMetadata) {
        store.updateData { list ->
            list + MediaCacheSave(episodeMetadata, MediaCacheMetadata(emptyList()))
        }
    }

    override suspend fun updateMediaCacheMetadata(
        targetEpisodeKey: EpisodeMetadata,
        newPartMetadata: MediaCachePartMetadata
    ) {
        store.updateData { currentList ->
            currentList.map { episode ->
                if (isSameEpisode(episode, targetEpisodeKey)) {
                    // This is the episode we want to update
                    val updatedPartMetadataList = episode.metadata.metadata + newPartMetadata
                    val updatedEpisodeMetadata =
                        episode.metadata.copy(metadata = updatedPartMetadataList)
                    episode.copy(metadata = updatedEpisodeMetadata)
                } else {
                    // This is not the episode we're looking for, return it as is
                    episode
                }
            }
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


    companion object {
        private val logger = logger<DataStoreMediaCacheDataSource>()
    }
}