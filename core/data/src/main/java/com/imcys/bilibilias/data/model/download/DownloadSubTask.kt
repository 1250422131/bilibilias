package com.imcys.bilibilias.data.model.download

import com.imcys.bilibilias.database.entity.download.DownloadStage
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.database.entity.download.DownloadSubTaskType
import java.util.Date

data class DownloadSubTask(
    val segmentId: Long,
    val downloadUrl: String,
    val savePath: String,
    val progress: Double = 0.0,
    val subTaskType: DownloadSubTaskType,
    val downloadState: DownloadState = DownloadState.WAITING,
    val createTime: Date = Date(),
    val updateTime: Date = Date(),
)