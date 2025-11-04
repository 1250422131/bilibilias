package com.imcys.bilibilias.dwonload.service


import android.Manifest.*
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.*
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.imcys.bilibilias.MainActivity
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.utils.DOWNLOAD_NOTIFICATION_CHANNEL_ID


class DownloadService : Service() {

    companion object {
        const val DOWNLOAD_SERVICE_ID = 100
    }

    lateinit var notificationCompat: NotificationCompat.Builder

    inner class DownloadBinder : Binder() {
        val service: DownloadService?
            get() = this@DownloadService
    }

    private val binder = DownloadBinder()

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // 防止用户突然进后台
        runCatching { startForeground() }
        return START_STICKY
    }

    fun startForeground() {
        // 构造通知
        notificationCompat = buildDownloadFileNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(permission.FOREGROUND_SERVICE) != PERMISSION_GRANTED) {
                stopSelf()
                return
            }
        }
        // 启动前台服务
        ServiceCompat.startForeground(
            this,
            DOWNLOAD_SERVICE_ID,
            notificationCompat.build(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            }
        )

    }

    /**
     * 构造通知
     */
    private fun buildDownloadFileNotification() =
        run {
            val intent = Intent(this, MainActivity::class.java)
            val pIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            NotificationCompat.Builder(
                this,
                DOWNLOAD_NOTIFICATION_CHANNEL_ID
            ).apply {
                setContentTitle(getString(R.string.download_cache_1))
                setContentText(getString(R.string.download_video))
                    .setProgress(100, 0, false)
                setContentIntent(pIntent)
                setSmallIcon(R.drawable.ic_logo_mini)
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                setOnlyAlertOnce(true)
            }
        }

    fun updateNotification(
        title: String,
        text: String,
        progress: Int,
        indeterminate: Boolean = false
    ) {
        notificationCompat.setContentTitle(title)
            .setContentText(text)
            .setProgress(100, progress, indeterminate)
        ServiceCompat.startForeground(
            this,
            DOWNLOAD_SERVICE_ID,
            notificationCompat.build(),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            } else {
                0
            }
        )
    }

    fun onDownloadFinished() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}
