package com.imcys.bilibilias.core.data

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.coroutines.AsDispatchers
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.media.cache.DataStoreMediaCacheStorage
import com.imcys.bilibilias.core.media.cache.MediaCacheSave
import com.imcys.bilibilias.core.media.cache.MediaCacheStorage
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.builtins.ListSerializer

object DataStoreProvider {
    val httpDownloader: HttpDownloader by lazy {
        KtorPersistentHttpDownloader(
            dataStore = DataStoreFactory.new(
                ListSerializer(DownloadState.serializer()).asDataStoreSerializer { emptyList() },
                produceFile = { resolveDataStoreFile("ktor_persistent_http_downloader") },
                corruptionHandler = ReplaceFileCorruptionHandler {
                    emptyList()
                },
                scope = CoroutineScope(AsDispatchers.applicationScope.coroutineContext + AsDispatchers.IO),
            ),
            client = createHttpClient {
                defaultRequest {
                    header(HttpHeaders.Accept, "*/*")
                    header(HttpHeaders.Referrer, "https://www.bilibili.com")
                    header(HttpHeaders.Origin, "https://www.bilibili.com")
                    header(HttpHeaders.Connection, "keep-alive")
                }
            },
            fileSystem = SystemFileSystem,
            baseSaveDir = Path(KmpContext.dataDir, "Download"),
        )
    }

    val mediaCacheStorage: MediaCacheStorage by lazy {
        DataStoreMediaCacheStorage(
            store = DataStoreFactory.new(
                serializer = ListSerializer(MediaCacheSave.serializer()).asDataStoreSerializer { emptyList() },
                corruptionHandler = ReplaceFileCorruptionHandler { emptyList() },
                produceFile = { resolveDataStoreFile("media_cache_storage") },
                scope = CoroutineScope(AsDispatchers.applicationScope.coroutineContext + AsDispatchers.IO)
            )
        )
    }
}