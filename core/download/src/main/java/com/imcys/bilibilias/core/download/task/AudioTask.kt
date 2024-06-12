package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewInfo
import java.io.File

class AudioTask(
    streamUrl: VideoStreamUrl,
    info: ViewInfo,
    page: ViewDetail.Pages,
    path: String,
) : AsDownloadTask(info, streamUrl, page.part) {
    override val priority = 100
    override val fileType = FileType.AUDIO
    override val destFile = File(path, "audio.aac")
    override val okTask = createTask(downloadUrl, destFile, priority)
    override fun getStrategy(streamUrl: VideoStreamUrl): String {
        return streamUrl.dash.audio.maxBy { it.id }.baseUrl
    }
}
