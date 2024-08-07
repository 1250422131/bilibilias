package com.imcys.bilibilias.core.download.task

import android.net.Uri
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewIds
import com.imcys.bilibilias.core.network.api.BILIBILI_URL
import com.imcys.bilibilias.core.network.api.BROWSER_USER_AGENT
import com.liulishuo.okdownload.DownloadTask

class AsDownloadTask(
    internal val ids: ViewIds,
    internal val title: String,
    internal val subTitle: String,
    internal val fileType: FileType,
    url: String,
    uri: Uri,
) {
    internal val okTask: DownloadTask = createTask(fileType, DownloadTask.Builder(url, uri))

    private fun createTask(
        fileType: FileType,
        builder: DownloadTask.Builder,
    ): DownloadTask = builder
        .setPassIfAlreadyCompleted(false)
        .setAutoCallbackToUIThread(false)
        .setMinIntervalMillisCallbackProcess(50)
        .addHeader("User-Agent", BROWSER_USER_AGENT)
        .addHeader("Referer", BILIBILI_URL)
        .setConnectionCount(1)
        .setPriority(fileType.priority)
        .build()
}
