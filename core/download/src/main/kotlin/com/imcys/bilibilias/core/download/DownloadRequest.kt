package com.imcys.bilibilias.core.download

import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.ViewInfo
import dev.DevUtils
import java.io.File

data class DownloadRequest(
    val viewInfo: ViewInfo,
    val format: Format,
)

data class Format(val codecid: Int, val taskType: TaskType, val quality: Int)

internal fun DownloadRequest.buildFilename() =
    "${DevUtils.getContext().downloadDir}_${viewInfo.bvid}_"

internal fun DownloadRequest.buildFullPath() =
    "${buildBasePath()}${File.separator}${format.quality}"

internal fun DownloadRequest.buildBasePath(): String = "${DevUtils.getContext().downloadDir}${File.separator}${viewInfo.aid}${File.separator}c_${viewInfo.cid}"
