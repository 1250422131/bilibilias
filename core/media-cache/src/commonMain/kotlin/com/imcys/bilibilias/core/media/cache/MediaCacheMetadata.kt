package com.imcys.bilibilias.core.media.cache

import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.io.resolve
import kotlinx.io.files.Path
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Instant

fun mediaCacheMetadata(
    vararg partMetadata: MediaCachePartMetadata,
    extra: Map<MetadataKey, String> = emptyMap()
): MediaCacheMetadata =
    MediaCacheMetadata(partMetadata.toList(), extra = extra)

@ConsistentCopyVisibility
@Serializable
data class MediaCacheMetadata internal constructor(
    val metadata: List<MediaCachePartMetadata>,
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
data class MediaCachePartMetadata(
    val downloadId: String
) {
    val filePath: Path
        get() = baseSaveDir.resolve(downloadId)

    companion object {
        private val baseSaveDir = Path(KmpContext.dataDir, "Download")
    }
}

@Serializable
@JvmInline
value class MetadataKey(val value: String)
