package com.imcys.bilibilias.core.datastore.model

import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.io.resolve
import com.imcys.bilibilias.core.logging.logger
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.time.Clock
import kotlin.time.Instant

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

@ConsistentCopyVisibility
@Serializable
data class MediaCacheMetadata internal constructor(
    val metadata: List<MediaCachePartMetadata>,
    val createdAt: Instant = Clock.System.now(),
    val extra: Map<MetadataKey, String> = emptyMap(),
) {
    @Transient
    private val logger = logger<MediaCacheMetadata>()
    fun delete(): Boolean {
        var allDeleted = true
        metadata.forEach { partMetadata ->
            val path = partMetadata.filePath
            try {
                if (SystemFileSystem.exists(path)) {
                    SystemFileSystem.delete(path)
                } else {
                    logger.warn { "Warning: File not found, skipping delete: $path" }
                }
            } catch (e: IOException) {
                logger.error(e) { "Delete failed for file: $path" }
                allDeleted = false
            }
        }
        return allDeleted
    }

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