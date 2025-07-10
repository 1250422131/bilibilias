package com.imcys.bilibilias.logic.utils

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.datastore.asDataStoreSerializer
import com.imcys.bilibilias.core.datastore.new
import com.imcys.bilibilias.core.datastore.resolveDataStoreFile
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.KtorPersistentHttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.builtins.ListSerializer
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun createKtorPersistentHttpDownloader(): HttpDownloader {
    return KtorPersistentHttpDownloader(
        dataStore = DataStoreFactory.new(
            ListSerializer(DownloadState.serializer()).asDataStoreSerializer { emptyList() },
            produceFile = { resolveDataStoreFile("ktor_persistent_http_downloader") },
            corruptionHandler = ReplaceFileCorruptionHandler {
                emptyList()
            },
        ),
        client = createHttpClient { },
        fileSystem = SystemFileSystem,
        baseSaveDir = Path(KmpContext.dataDir, "Download")
    )
}