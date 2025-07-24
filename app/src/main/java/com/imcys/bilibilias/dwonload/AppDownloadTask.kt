package com.imcys.bilibilias.dwonload

import com.imcys.bilibilias.data.model.download.DownloadSubTask
import com.imcys.bilibilias.database.entity.download.DownloadSegment
import com.imcys.bilibilias.database.entity.download.DownloadStage
import com.imcys.bilibilias.database.entity.download.DownloadState
import com.imcys.bilibilias.database.entity.download.DownloadTask

data class AppDownloadTask(
    val downloadTask: DownloadTask,
    val downloadSegment: DownloadSegment,
    val downloadSubTasks: List<DownloadSubTask>,
    val downloadStage: DownloadStage = DownloadStage.DOWNLOAD,
    val cover: String?,
    val progress: Double = 0.0,
    val downloadState: DownloadState = DownloadState.WAITING,
)