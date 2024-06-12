package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.model.video.ViewInfo
import java.io.File

class VideoTask(
    streamUrl: VideoStreamUrl,
    info: ViewInfo,
    page: ViewDetail.Pages,
    path: String,
    private val quality: Int,
    private val codecid: Int,
) : AsDownloadTask(info, streamUrl, page.part) {

    override val priority = 99
    override val fileType = FileType.VIDEO
    override val destFile = File(path, "video.mp4")
    override val okTask = createTask(downloadUrl, destFile, priority)
    override fun getStrategy(streamUrl: VideoStreamUrl): String {
        val videos = streamUrl.dash.video.groupBy { it.id }[quality]
            ?: error("没有所选清晰度")
        val v = videos.singleOrNull { it.codecid == codecid }
            ?: videos.maxBy { it.codecid }
        return v.baseUrl
    }
}
