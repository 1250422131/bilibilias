package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import java.io.File

class VideoTask(
    streamUrl: VideoStreamUrl,
    request: DownloadRequest,
    page: ViewDetail.Pages,
    path: String,
) : AsDownloadTask(request.viewInfo, streamUrl, request, page.part) {

    override val priority = 99
    override val fileType = FileType.VIDEO
    override val destFile = File(path, "video.mp4")
    override val okTask = createTask(downloadUrl, destFile, priority)
    override fun getStrategy(streamUrl: VideoStreamUrl, request: DownloadRequest): String {
        val videos = streamUrl.dash.video.groupBy { it.id }[request.format.quality]
            ?: error("没有所选清晰度")
        val v = videos.singleOrNull { it.codecid == request.format.codecid }
            ?: videos.maxBy { it.codecid }
        return v.baseUrl
    }
}
