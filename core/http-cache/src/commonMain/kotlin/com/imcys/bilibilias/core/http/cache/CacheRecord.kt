package com.imcys.bilibilias.core.http.cache

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

class CacheRecord {

    fun cache() {

    }
}

@Serializable
data class MediaFileMetadata(
    val title: String,
    val createAt: LocalDateTime,
    val parts: List<MediaPart>,
    val extras: Map<MetadataKey, String> = emptyMap(),
)

@Serializable
@JvmInline
value class MetadataKey(val value: String)

@Serializable
data class MediaPart(
    val uri: String,
    val kind: MediaType
)

enum class MediaType {
    AUDIO, VIDEO, SUBTITLE
}