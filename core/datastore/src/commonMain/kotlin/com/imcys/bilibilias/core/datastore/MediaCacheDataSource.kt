package com.imcys.bilibilias.core.datastore

import com.imcys.bilibilias.core.datastore.model.EpisodeMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCachePartMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCacheSave
import kotlinx.coroutines.flow.Flow

interface MediaCacheDataSource {
    val listFlow: Flow<List<MediaCacheSave>>

    suspend fun delete(episodeMetadata: EpisodeMetadata): Boolean

    suspend fun cacheEpisodeMetadata(episodeMetadata: EpisodeMetadata)

    suspend fun updateMediaCacheMetadata(
        targetEpisodeKey: EpisodeMetadata,
        newPartMetadata: MediaCachePartMetadata
    )
}