package com.imcys.bilibilias.core.notifications

import com.imcys.bilibilias.core.model.download.DownloadTask

/**
 * Aggregates news resources that have been notified for addition
 */
class TestNotifier : Notifier {

    private val mutableAddedNewResources = mutableListOf<List<DownloadTask>>()

    val addedNewsResources: List<List<DownloadTask>> = mutableAddedNewResources

    override fun postNewsNotifications(tasks: List<DownloadTask>) {
        mutableAddedNewResources.add(tasks)
    }
}
