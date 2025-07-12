package com.imcys.bilibilias.core.http.cache

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaCacheMetadata(
    @SerialName("bvid")
    val episodeId: String,
    val title: String,
    val extra: Map<MetadataKey, String> = emptyMap(),
) {
    fun withExtra(other: Map<MetadataKey, String>): MediaCacheMetadata {
        return MediaCacheMetadata(
            episodeId = episodeId,
            extra = extra + other,
        )
    }

    companion object {
        val KEY_CREATION_TIME = MetadataKey("creationTime")
    }
}

@Serializable
@JvmInline
value class MetadataKey(val value: String)
