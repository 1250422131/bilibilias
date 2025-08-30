package com.imcys.bilibilias.notifications

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.InboxStyle
import androidx.core.app.NotificationManagerCompat
import com.imcys.bilibilias.R

private const val TARGET_ACTIVITY_NAME = "com.imcys.bilibilias.MainActivity"
private const val NOTIFICATION_REQUEST_CODE = 0
private const val NOTIFICATION_SUMMARY_ID = 1
private const val NOTIFICATION_CHANNEL_ID = ""
private const val NOTIFICATION_GROUP = "NOTIFICATIONS"

interface Notifier {
    fun postNotifications()
}

internal class SystemTrayNotifier(
    private val context: Context,
) : Notifier {
    override fun postNotifications() = with(context) {
        if (checkSelfPermission(this, permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
            return
        }
        val notification = createNotification {
            setSmallIcon(R.drawable.bilibilias)
                .setContentIntent(newsPendingIntent())
                .setGroup(NOTIFICATION_GROUP)
                .setAutoCancel(true)
        }
        val summaryNotification = createNotification {
            val title = getString(R.string.notification_title)
            setContentTitle(title)
                .setContentText(title)
                .setSmallIcon(R.drawable.bilibilias)
                // Build summary info into InboxStyle template.
                .setStyle(newsNotificationStyle(title))
                .setGroup(NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build()
        }
        // Send the notifications
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, notification)

        notificationManager.notify(NOTIFICATION_SUMMARY_ID, summaryNotification)
    }

    /**
     * Creates an inbox style summary notification for news updates
     */
    private fun newsNotificationStyle(
        title: String,
    ): InboxStyle = InboxStyle()
        .setBigContentTitle(title)
        .setSummaryText(title)
}

/**
 * Creates a notification for configured for news updates
 */
private fun Context.createNotification(
    block: NotificationCompat.Builder.() -> Unit,
): Notification {
    ensureNotificationChannelExists()
    return NotificationCompat.Builder(
        this,
        NOTIFICATION_CHANNEL_ID,
    )
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .apply(block)
        .build()
}

/**
 * Ensures that a notification channel is present if applicable
 */
private fun Context.ensureNotificationChannelExists() {
    if (VERSION.SDK_INT < VERSION_CODES.O) return

    val channel = NotificationChannel(
        NOTIFICATION_CHANNEL_ID,
        getString(R.string.notification_channel_name),
        NotificationManager.IMPORTANCE_DEFAULT,
    ).apply {
        description = getString(R.string.notification_channel_description)
    }
    // Register the channel with the system
    NotificationManagerCompat.from(this).createNotificationChannel(channel)
}

private fun Context.newsPendingIntent(): PendingIntent? = PendingIntent.getActivity(
    this,
    NOTIFICATION_REQUEST_CODE,
    Intent().apply {
        action = Intent.ACTION_VIEW
        component = ComponentName(
            packageName,
            TARGET_ACTIVITY_NAME,
        )
    },
    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
)