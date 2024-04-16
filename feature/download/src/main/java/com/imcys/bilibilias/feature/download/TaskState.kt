package com.imcys.bilibilias.feature.download

import com.imcys.bilibilias.core.network.download.FileType
import com.imcys.bilibilias.core.network.download.Task

data class TaskState(
    val title: String,
    val type: FileType,
    val bvid: String,
    val cid: Long,
    val progress: () -> Float,
    val isRunning: () -> Boolean
)

fun Task.mapToTaskState(): TaskState {
    return TaskState(title, type, groupTag, groupId, { progress }, { isRunning })
}
