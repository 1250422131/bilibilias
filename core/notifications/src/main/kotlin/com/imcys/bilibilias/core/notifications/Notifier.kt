package com.imcys.bilibilias.core.notifications

import com.imcys.bilibilias.core.model.download.DownloadTask

/**
 * Interface for creating notifications in the app
 */
interface Notifier {
    fun postNewsNotifications(tasks: List<DownloadTask>)
}
