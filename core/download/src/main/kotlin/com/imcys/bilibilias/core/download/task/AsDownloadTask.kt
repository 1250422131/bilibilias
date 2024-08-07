package com.imcys.bilibilias.core.download.task

import android.net.Uri
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask

class AsDownloadTask {
    internal val viewInfo: ViewInfo
    internal val subTitle: String
    internal val okTask: DownloadTask
    internal val fileType: FileType

    constructor(viewInfo: ViewInfo, subTitle: String, fileType: FileType, url: String, uri: Uri) {
        this.viewInfo = viewInfo
        this.subTitle = subTitle
        this.fileType = fileType
        this.okTask = createTask(fileType, DownloadTask.Builder(url, uri))
    }

    private fun createTask(
        fileType: FileType,
        builder: DownloadTask.Builder,
    ): DownloadTask = builder
        .setPassIfAlreadyCompleted(false)
        .setAutoCallbackToUIThread(true)
        .setMinIntervalMillisCallbackProcess(50)
        .addHeader(
            "User-Agent",
            "Mozilla/4.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36",
        )
        .addHeader("Referer", "https://www.bilibili.com/")
        .setConnectionCount(1)
        .setPriority(fileType.priority)
        .build()

    override fun toString(): String = "AsDownloadTask(subTitle='$subTitle', viewInfo=$viewInfo)"
}
