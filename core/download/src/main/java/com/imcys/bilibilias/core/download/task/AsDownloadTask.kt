package com.imcys.bilibilias.core.download.task

import android.net.Uri
import com.imcys.bilibilias.core.common.utils.DataSize.Companion.mb
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask
import java.io.File

class AsDownloadTask {
    internal val viewInfo: ViewInfo
    internal val subTitle: String
    internal val okTask: DownloadTask
    internal val fileType: FileType

    constructor(viewInfo: ViewInfo, subTitle: String, fileType: FileType, url: String, file: File) {
        this.viewInfo = viewInfo
        this.subTitle = subTitle
        this.fileType = fileType
        this.okTask = createTask(fileType, DownloadTask.Builder(url, file))
    }

    constructor(viewInfo: ViewInfo, subTitle: String, fileType: FileType, url: String, uri: Uri) {
        this.viewInfo = viewInfo
        this.subTitle = subTitle
        this.fileType = fileType
        this.okTask = createTask(fileType, DownloadTask.Builder(url, uri))
    }

    private fun createTask(
        fileType: FileType,
        builder: DownloadTask.Builder
    ): DownloadTask {
        return builder
            .setPassIfAlreadyCompleted(false)
            .setAutoCallbackToUIThread(false)
            .setMinIntervalMillisCallbackProcess(50)
            .setReadBufferSize(16.mb.inWholeBytes.toInt())
            .setFlushBufferSize(32.mb.inWholeBytes.toInt())
            .setHeaderMapFields(
                mapOf(
                    "User-Agent" to
                            listOf("Mozilla/4.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"),
                    "Referer" to listOf("https://www.bilibili.com/")
                )
            )
            .setConnectionCount(1)
//            .setFilename(fileType.filename)
            .setPriority(fileType.priority)
            .build()
    }

    override fun toString(): String {
        return "AsDownloadTask(subTitle='$subTitle', viewInfo=$viewInfo)"
    }
}
