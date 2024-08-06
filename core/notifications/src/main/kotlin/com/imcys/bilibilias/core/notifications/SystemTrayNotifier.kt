package com.imcys.bilibilias.core.notifications

// import android.Manifest
// import android.Manifest.permission
// import android.app.Notification
// import android.app.NotificationChannel
// import android.app.NotificationManager
// import android.app.PendingIntent
// import android.content.ComponentName
// import android.content.Context
// import android.content.Intent
// import android.content.pm.PackageManager.PERMISSION_GRANTED
// import android.os.Build
// import android.os.Build.VERSION
// import android.os.Build.VERSION_CODES
// import androidx.core.app.ActivityCompat.checkSelfPermission
// import androidx.core.app.NotificationCompat
// import androidx.core.app.NotificationCompat.InboxStyle
// import androidx.core.app.NotificationManagerCompat
// import androidx.core.net.toUri
// import dagger.hilt.android.qualifiers.ApplicationContext
// import javax.inject.Inject
// import javax.inject.Singleton

private const val MAX_NUM_NOTIFICATIONS = 5
private const val TARGET_ACTIVITY_NAME = "com.imcys.bilibilias.MainActivity"
private const val NEWS_NOTIFICATION_REQUEST_CODE = 0
private const val NEWS_NOTIFICATION_SUMMARY_ID = 1
private const val NEWS_NOTIFICATION_CHANNEL_ID = ""
private const val NEWS_NOTIFICATION_GROUP = "NEWS_NOTIFICATIONS"
private const val FOR_YOU_PATH = "foryou"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
// @Singleton
// internal class SystemTrayNotifier @Inject constructor(
//    @ApplicationContext private val context: Context,
// ) : Notifier {
//
//    override fun postNewsNotifications(
//        newsResources: List<NewsResource>,
//    ) = with(context) {
//        if (checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PERMISSION_GRANTED) {
//            return
//        }
//
//        val truncatedNewsResources = newsResources.take(MAX_NUM_NOTIFICATIONS)
//
//        val newsNotifications = truncatedNewsResources.map { newsResource ->
//            createNewsNotification {
//                setSmallIcon(R.drawable.core_notifications_ic_nia_notification)
//                    .setContentTitle(newsResource.title)
//                    .setContentText(newsResource.content)
//                    .setContentIntent(newsPendingIntent(newsResource))
//                    .setGroup(NEWS_NOTIFICATION_GROUP)
//                    .setAutoCancel(true)
//            }
//        }
//        val summaryNotification = createNewsNotification {
//            val title = getString(
//                R.string.core_notifications_news_notification_group_summary,
//                truncatedNewsResources.size,
//            )
//            setContentTitle(title)
//                .setContentText(title)
//                .setSmallIcon(R.drawable.core_notifications_ic_nia_notification)
//                // Build summary info into InboxStyle template.
//                .setStyle(newsNotificationStyle(truncatedNewsResources, title))
//                .setGroup(NEWS_NOTIFICATION_GROUP)
//                .setGroupSummary(true)
//                .setAutoCancel(true)
//                .build()
//        }
//
//        // Send the notifications
//        val notificationManager = NotificationManagerCompat.from(this)
//        newsNotifications.forEachIndexed { index, notification ->
//            notificationManager.notify(
//                truncatedNewsResources[index].id.hashCode(),
//                notification,
//            )
//        }
//        notificationManager.notify(NEWS_NOTIFICATION_SUMMARY_ID, summaryNotification)
//    }
//
//    /**
//     * Creates an inbox style summary notification for news updates
//     */
//    private fun newsNotificationStyle(
//        newsResources: List<NewsResource>,
//        title: String,
//    ): InboxStyle = newsResources
//        .fold(Notification.InboxStyle()) { inboxStyle, newsResource -> inboxStyle.addLine(newsResource.title) }
//        .setBigContentTitle(title)
//        .setSummaryText(title)
// }

/**
 * Creates a notification for configured for news updates
 */
// private fun Context.createNewsNotification(
//    block: NotificationCompat.Builder.() -> Unit,
// ): Notification {
//    ensureNotificationChannelExists()
//    return NotificationCompat.Builder(
//        this,
//        NEWS_NOTIFICATION_CHANNEL_ID,
//    )
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        .apply(block)
//        .build()
// }

// /**
// * Ensures that a notification channel is present if applicable
// */
// private fun Context.ensureNotificationChannelExists() {
//    if (VERSION.SDK_INT < VERSION_CODES.O) return
//
//    val channel = NotificationChannel(
//        NEWS_NOTIFICATION_CHANNEL_ID,
//        getString(R.string.core_notifications_news_notification_channel_name),
//        NotificationManager.IMPORTANCE_DEFAULT,
//    ).apply {
//        description = getString(R.string.core_notifications_news_notification_channel_description)
//    }
//    // Register the channel with the system
//    NotificationManagerCompat.from(this).createNotificationChannel(channel)
// }
//
// private fun Context.newsPendingIntent(
//    newsResource: NewsResource,
// ): PendingIntent? = PendingIntent.getActivity(
//    this,
//    NEWS_NOTIFICATION_REQUEST_CODE,
//    Intent().apply {
//        action = Intent.ACTION_VIEW
//        data = newsResource.newsDeepLinkUri()
//        component = ComponentName(
//            packageName,
//            TARGET_ACTIVITY_NAME,
//        )
//    },
//    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
// )
//
// private fun NewsResource.newsDeepLinkUri() = "$DEEP_LINK_SCHEME_AND_HOST/$FOR_YOU_PATH/$id".toUri()
