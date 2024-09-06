package com.imcys.bilibilias.core.notifications

import com.imcys.bilibilias.core.model.download.DownloadTask
import javax.inject.Inject

/**
 * Implementation of [Notifier] which does nothing. Useful for tests and previews.
 */
internal class NoOpNotifier @Inject constructor() : Notifier {
    override fun postNewsNotifications(tasks: List<DownloadTask>) = Unit
}
