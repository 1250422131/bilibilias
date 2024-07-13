package com.imcys.bilibilias.core.download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

val GROUP_KEY_DOWNLOAD_EMAIL = "com.imcys.bilibilias.DOWNLOAD_GROUP"
val CHANNEL_ID_DOWNLOAD = "com.imcys.bilibilias.DOWNLOAD_CHANNEL"

class DownloadNotification @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val builder = NotificationCompat.Builder(context, CHANNEL_ID_DOWNLOAD)
        .setGroup(GROUP_KEY_DOWNLOAD_EMAIL)
        .setPriority(NotificationCompat.PRIORITY_LOW)

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID_DOWNLOAD, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)
        }
    }
}
