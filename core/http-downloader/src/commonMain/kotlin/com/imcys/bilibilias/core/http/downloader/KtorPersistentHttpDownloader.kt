package com.imcys.bilibilias.core.http.downloader

import androidx.datastore.core.DataStore
import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.withLock
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlin.coroutines.CoroutineContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * A persistent version of [KtorHttpDownloader] that automatically:
 * - Loads saved download states from [dataStore] on construction.
 * - Saves new/updated states whenever [_downloadStatesFlow] changes.
 */
@OptIn(ExperimentalTime::class)
class KtorPersistentHttpDownloader(
    private val dataStore: DataStore<List<DownloadState>>,
    client: HttpClient,
    fileSystem: FileSystem,
    baseSaveDir: Path,
    computeDispatcher: CoroutineContext = Dispatchers.Default,
    ioDispatcher: CoroutineContext = Dispatchers.IO,
    clock: Clock = Clock.System,
) : KtorHttpDownloader(
    client = client,
    fileSystem = fileSystem,
    baseSaveDir = baseSaveDir,
    computeDispatcher = computeDispatcher,
    ioDispatcher = ioDispatcher,
    clock = clock,
) {
    override suspend fun init() {
        super.init()
        scope.launch(
            CoroutineName("KtorPersistentHttpDownloader.saver"),
            start = CoroutineStart.UNDISPATCHED, // register job now
        ) {
            downloadStatesFlow.buffer(
                capacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST, // 当来不及保存时, 不需要保存中间状态
            ).collect { states ->
                dataStore.updateData { states } // override the entire list
            }
        }
        restoreStates()
    }

    /**
     * Replaces the current in-memory map with data loaded from [dataStore], but does not resume them.
     * To resume downloads, call [KtorHttpDownloader.resume] for each entry in the restored map.
     */
    private suspend fun restoreStates() {
        val savedList: List<DownloadState> = dataStore.data.first()
        stateMutex.withLock {
            val currentMap: MutableMap<DownloadId, DownloadEntry> = LinkedHashMap(savedList.size)

            savedList.forEach { st ->
                currentMap[st.downloadId] = DownloadEntry(
                    job = null,
                    state = st.copy(
                        status = when (val status = st.status) {
                            // 恢复时必须将原本的下载中状态设置为 PAUSED, 否则无法 resume.
                            DownloadStatus.INITIALIZING,
                            DownloadStatus.DOWNLOADING,
                            DownloadStatus.MERGING -> DownloadStatus.PAUSED

                            DownloadStatus.PAUSED,
                            DownloadStatus.COMPLETED,
                            DownloadStatus.FAILED,
                            DownloadStatus.CANCELED -> status
                        },
                    ),
                )
            }
            _downloadStatesFlow.value = currentMap
            logger.i { "Restored ${currentMap.size} downloads from DataStore" }
        }
    }

    private companion object {
        private val logger = Logger.withTag("KtorPersistentHttpDownloader")
    }
}