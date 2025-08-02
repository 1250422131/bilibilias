package com.imcys.bilibilias

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheDataSource: MediaCacheDataSource,
    private val applicationScope: CoroutineScope,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        applicationScope.launch {
            httpDownloader.downloadStatesFlow
                .collect { states -> // Renamed 'it' to 'states' for clarity
                    states.filter { downloadState -> downloadState.status == DownloadStatus.PAUSED } // Renamed 'it'
                        .forEach { downloadState -> httpDownloader.resume(downloadState.downloadId) }
                }
        }
        return Result.success()
    }

    // 恢复逻辑
    //        val result = combine(
//            httpDownloader.downloadStatesFlow,
//            mediaCacheDataSource.listFlow
//        ) { downloadStates, mediaCacheSaves ->
//            val nowMillis = Clock.System.now().toEpochMilliseconds()
//            val twoHoursMillis = 2.hours.inWholeMilliseconds
//
//            val staleDownloadIds = downloadStates
//                .filter { state ->
//                    state.status != DownloadStatus.COMPLETED &&
//                            state.timestamp + twoHoursMillis < nowMillis
//                }
//                .map { it.downloadId.value }
//                .toSet()
//
//            if (staleDownloadIds.isEmpty()) {
//                emptyList()
//            } else {
//                mediaCacheSaves.filter { save ->
//                    save.metadata.metadata.any { metadataItem ->
//                        metadataItem.downloadId in staleDownloadIds
//                    }
//                }
//            }
//        }
    companion object {
        fun startWork() = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>()
            .build()
    }
}