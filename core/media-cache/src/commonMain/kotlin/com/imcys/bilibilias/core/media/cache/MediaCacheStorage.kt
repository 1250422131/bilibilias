package com.imcys.bilibilias.core.media.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface MediaCacheStorage : AutoCloseable {
    val listFlow: Flow<List<MediaCache>>
    suspend fun cache(
        episodeMetadata: EpisodeMetadata,
        metadata: MediaCacheMetadata,
        resume: Boolean = false,
    ): MediaCache
}

@Serializable
data class MediaCacheSave(
    val origin: EpisodeMetadata,
    val metadata: MediaCacheMetadata,
)

@Serializable
data class EpisodeMetadata(
    val bvid: String,
    val cid: Long,
    val title: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EpisodeMetadata

        if (cid != other.cid) return false
        if (bvid != other.bvid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cid.hashCode()
        result = 31 * result + bvid.hashCode()
        return result
    }

}