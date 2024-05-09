package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.common.utils.DataSize.Companion.mb
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask
import java.io.File

sealed class AsDownloadTask(
    val viewInfo: ViewInfo,
    streamUrl: VideoStreamUrl,
    request: DownloadRequest,
    val subTitle: String,
) {
    internal abstract val priority: Int
    internal abstract val destFile: File
    abstract val fileType: FileType
    internal abstract val okTask: DownloadTask
    internal val downloadUrl = getStrategy(streamUrl, request)
    protected fun createTask(
        url: String,
        file: File,
        priority: Int,
    ): DownloadTask {
        return DownloadTask.Builder(url, file)
            .setPassIfAlreadyCompleted(false)
            .setAutoCallbackToUIThread(false)
            .setMinIntervalMillisCallbackProcess(500)
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
            .setPriority(priority)
            .build()
    }

    abstract fun getStrategy(streamUrl: VideoStreamUrl, request: DownloadRequest): String
    override fun toString(): String {
        return "AsDownloadTask(viewInfo=$viewInfo, okTask=$okTask)"
    }
}
