package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.download.buildFullPath
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import java.io.File

class AudioTask(
    streamUrl: VideoStreamUrl,
    request: DownloadRequest,
    page: ViewDetail.Pages
) : AsDownloadTask(request.viewInfo, streamUrl, request, page.part) {
    override val priority = 100
    override val fileType = FileType.AUDIO
    override val destFile = File(request.buildFullPath(), "audio.mp4")
    override val okTask = createTask(downloadUrl, destFile, priority)
    override fun getStrategy(streamUrl: VideoStreamUrl, request: DownloadRequest): String {
        return streamUrl.dash.audio.maxBy { it.id }.baseUrl
    }
}
