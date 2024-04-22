package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.download.State
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.StatusUtil
import com.liulishuo.okdownload.kotlin.DownloadProgress
import com.liulishuo.okdownload.kotlin.progressFlow
import kotlinx.coroutines.flow.Flow
import java.io.File

class VideoTask(url: String, path: String, viewInfo: ViewInfo) :
    AsDownloadTask(viewInfo) {
    override val priority = 99
    override val fileType = FileType.VIDEO
    override val destFile = File(path, "video.mp4")
    override val task = createTask(url, destFile, priority, viewInfo, fileType)
    override val state: State = getState(task)
    override val isCompleted: Boolean = StatusUtil.isCompleted(task)
    override val progress: Flow<DownloadProgress> = task.progressFlow()
    override fun toString(): String {
        return "AudioTask(fileType=$fileType, task=$task, state=$state, isCompleted=$isCompleted)"
    }
}
