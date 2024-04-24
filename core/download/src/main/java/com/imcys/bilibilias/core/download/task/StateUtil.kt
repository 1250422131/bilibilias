package com.imcys.bilibilias.core.download.task

import com.imcys.bilibilias.core.model.download.State
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.StatusUtil

internal fun getState(task: DownloadTask): State {
    return when (StatusUtil.getStatus(task)) {
        StatusUtil.Status.PENDING -> State.PENDING
        StatusUtil.Status.RUNNING -> State.RUNNING
        StatusUtil.Status.COMPLETED -> State.COMPLETED
        StatusUtil.Status.IDLE -> State.IDLE
        StatusUtil.Status.UNKNOWN -> State.UNKNOWN
        null -> error("获取的状态: null")
    }
}