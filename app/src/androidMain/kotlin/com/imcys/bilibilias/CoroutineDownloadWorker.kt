package com.imcys.bilibilias

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import kotlinx.coroutines.flow.combine
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheDataSource: MediaCacheDataSource
) : CoroutineWorker(context, params) {

    @OptIn(ExperimentalTime::class)
    override suspend fun doWork(): Result {
        val result = combine(
            httpDownloader.downloadStatesFlow,
            mediaCacheDataSource.listFlow
        ) { downloadStates, mediaCacheSaves ->
            val nowMillis = Clock.System.now().toEpochMilliseconds()
            val twoHoursMillis = 2.hours.inWholeMilliseconds

            val staleDownloadIds = downloadStates
                .filter { state ->
                    state.status != DownloadStatus.COMPLETED &&
                            state.timestamp + twoHoursMillis < nowMillis
                }
                .map { it.downloadId.value }
                .toSet()

            if (staleDownloadIds.isEmpty()) {
                emptyList()
            } else {
                mediaCacheSaves.filter { save ->
                    save.metadata.metadata.any { metadataItem ->
                        metadataItem.downloadId in staleDownloadIds
                    }
                }
            }
        }


        return Result.success()
    }

    companion object {
    }
}