package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.download.DownloadProgress
import com.imcys.bilibilias.core.download.extension.spChannel
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil
import kotlinx.coroutines.channels.Channel

sealed class AsDownloadTask(val viewInfo: ViewInfo) {
    internal open lateinit var task: DownloadTask
    abstract val priority: Int
    val state: State
        get() {
            checkInitialized()
            return when (StatusUtil.getStatus(task)) {
                StatusUtil.Status.PENDING -> State.PENDING
                StatusUtil.Status.RUNNING -> State.RUNNING
                StatusUtil.Status.COMPLETED -> State.COMPLETED
                StatusUtil.Status.IDLE -> State.IDLE
                StatusUtil.Status.UNKNOWN -> State.UNKNOWN
                null -> error("获取的状态: null")
            }
        }
    val isCompleted: Boolean
        get() {
            checkInitialized()
            return StatusUtil.isCompleted(task)
        }
    val progress: Channel<DownloadProgress>
        get() {
            checkInitialized()
            return task.spChannel()
        }

    protected fun checkInitialized() {
        if (!::task.isInitialized) error("未初始化 任务对象")
    }
}