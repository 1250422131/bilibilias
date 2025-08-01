package com.imcys.bilibilias

import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE

class ServiceNotification(
    private val context: Context,
) {
    private val notificationService by lazy { context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
}