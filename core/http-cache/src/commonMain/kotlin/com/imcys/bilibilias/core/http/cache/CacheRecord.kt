package com.imcys.bilibilias.core.http.cache

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.model.CacheData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

class CacheRecord {
    private val dataStore: DataStore<List<MediaFileMetadata>> =
        DataStoreFactory.new(
            ListSerializer(MediaFileMetadata.serializer()).asDataStoreSerializer { emptyList() },
            corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
            produceFile = { resolveDataStoreFile("cache_persistent_record") }
        )
    protected val _httpMediaCachFlow = MutableStateFlow(emptyList<MediaFileMetadata>())
    fun cache(data: CacheData, videoDownloadId: String, audioDownloadId: String) {

    }
}

@Serializable
data class MediaFileMetadata(
    val title: String,
    val createAt: LocalDateTime,
    val parts: List<MediaPart>,
    val extras: Map<MetadataKey, String> = emptyMap(),
) {
    companion object {
        val KEY_BVID = MetadataKey("bvid")
        val KEY_CID = MetadataKey("cid")
    }
}


@Serializable
data class MediaPart(
    val uri: String,
    val kind: MediaType
)

enum class MediaType {
    AUDIO, VIDEO, SUBTITLE
}