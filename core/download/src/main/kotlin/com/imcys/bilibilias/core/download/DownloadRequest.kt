package com.imcys.bilibilias.core.download

import com.imcys.bilibilias.core.model.download.TaskType
import com.imcys.bilibilias.core.model.video.ViewInfo

data class DownloadRequest(
    val viewInfo: ViewInfo,
    val format: Format,
)

data class Format(val codecid: Int, val taskType: TaskType, val quality: Int)
