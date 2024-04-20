package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.video.ViewInfo
import java.io.File

class Audio(val url: String, val path: File, viewInfo: ViewInfo) :
    AsDownloadTask(viewInfo) {
    override val priority = 100
    val destFile = File(path, "audio.mp4")
}
