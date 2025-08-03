package com.imcys.bilibilias.common.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "com.imcys.bilibilias.gp.DOWNLOAD_NOTIFICATION"

const val DOWNLOAD_NOTIFICATION_ID = 1000


fun Context.applyDownloadNotificationManager(notificationManagerContent: NotificationManagerCompat.() -> Unit) {
    NotificationManagerCompat.from(this).apply(notificationManagerContent)
}

fun Context.createDownloadNotificationChannel() {
    val channelId = DOWNLOAD_NOTIFICATION_CHANNEL_ID
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "视频缓存通知"
        val descriptionText = "方便观察下载进度"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            channelId,
            name,
            importance
        ).apply {
            description = descriptionText
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}