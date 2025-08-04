package com.imcys.bilibilias

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
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
                .collect { states ->
                    states.filter { downloadState -> downloadState.status == DownloadStatus.PAUSED }
                        .forEach { downloadState -> httpDownloader.resume(downloadState.downloadId) }
                }
        }
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            SYNC_NOTIFICATION_ID,
            applicationContext.workNotification(),
        )
    }

    /**
     * Notification displayed on lower API levels when sync workers are being
     * run with a foreground service
     */
    private fun Context.workNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.work_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = getString(R.string.work_notification_channel_description)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager? =
                getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager

            notificationManager?.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID,
        )
//            .setSmallIcon(
//                com.google.samples.apps.nowinandroid.core.notifications.R.drawable.core_notifications_ic_nia_notification,
//            )
            .setContentTitle(getString(R.string.work_notification_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
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
        const val SYNC_TOPIC = "sync"
        private const val SYNC_NOTIFICATION_ID = 0
        private const val NOTIFICATION_CHANNEL_ID = "NotificationChannel"
        fun startWork() = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>()
            .build()
    }
}