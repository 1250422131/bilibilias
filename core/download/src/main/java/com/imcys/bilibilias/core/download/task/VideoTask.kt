package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.download.buildFullPath
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import java.io.File

class VideoTask(
    streamUrl: VideoStreamUrl,
    request: DownloadRequest,
    val page: ViewDetail.Pages
) : AsDownloadTask(request.viewInfo, streamUrl, request, page.part) {

    override val priority = 99
    override val fileType = FileType.VIDEO
    override val destFile = File(request.buildFullPath(), "video.mp4")
    override val okTask = createTask(downloadUrl, destFile, priority)
    override fun getStrategy(streamUrl: VideoStreamUrl, request: DownloadRequest): String {
        val videos = streamUrl.dash.video.groupBy { it.id }[request.format.quality]
            ?: error("没有所选清晰度")
        return videos.single { it.codecid == request.format.codecid }.baseUrl
    }
}
