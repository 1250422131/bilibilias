package com.imcys.bilibilias.work

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
import com.imcys.bilibilias.R
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first

class CoroutineDownloadWorker(
    context: Context,
    params: WorkerParameters,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheDataSource: MediaCacheDataSource,
    private val applicationScope: CoroutineScope,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        httpDownloader.downloadStatesFlow.first()
            .filter { downloadState -> downloadState.status == DownloadStatus.PAUSED }
            .forEach { downloadState -> httpDownloader.resume(downloadState.downloadId) }
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
            .setSmallIcon(R.drawable.bilibilias)
            .setContentTitle(getString(R.string.work_notification_title))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
    companion object {
        const val SYNC_TOPIC = "sync"
        private const val SYNC_NOTIFICATION_ID = 0
        private const val NOTIFICATION_CHANNEL_ID = "NotificationChannel"
        fun startWork() = OneTimeWorkRequestBuilder<CoroutineDownloadWorker>()
            .build()
    }
}