package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.video.ViewInfo
import java.io.File

class Video(val url: String, val path: String, viewInfo: ViewInfo) :
    AsDownloadTask(viewInfo) {
    override val priority = 99
    val destFile = File(path, "video.mp4")
}
