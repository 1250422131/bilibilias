package com.imcys.bilibilias.core.network.download

import kotlinx.coroutines.Job
import java.io.File

enum class TaskType {
    VIDEO,
    AUDIO
}

data class Task(
    val url: String,
    val path: File,
    val type: TaskType,
    val groupId: Long = 0,
    val groupTag: String = ""
) {
    internal var job: Job? = null
    var progress = 0f
        internal set

    fun cancelTask() {
        requireNotNull(job).cancel()
    }
}
