package com.imcys.bilibilias.core.media.cache

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

@Serializable
data class MediaCacheMetadata(
    val downloadId1: String,
    val downloadId2: String,
    val outputPath1: String? = null,
    val outputPath2: String? = null,
    val createdAt: Instant = Clock.System.now(),
    val extra: Map<MetadataKey, String> = emptyMap(),
) {
    fun withExtra(other: Map<MetadataKey, String>): MediaCacheMetadata {
        return copy(
            extra = extra + other,
        )
    }
}

@Serializable
@JvmInline
value class MetadataKey(val value: String)
