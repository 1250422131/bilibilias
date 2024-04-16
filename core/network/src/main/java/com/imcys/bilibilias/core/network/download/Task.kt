package com.imcys.bilibilias.core.network.download

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Job
import java.io.File

enum class FileType {
    VIDEO,
    AUDIO
}

data class Task(
    val url: String,
    val path: File,
    val type: FileType,
    val title: String,
    val groupId: Long = 0,
    val groupTag: String = ""
) {
    var state by mutableStateOf<TaskState>(TaskState.Idle)
        internal set

    val isRunning by mutableStateOf(state is TaskState.Running)

    val isSuccess = state is TaskState.Success

    val isFailed = state is TaskState.Error

    var progress by mutableFloatStateOf(0f)
        internal set

    internal var job: Job? = null
    fun cancelTask() {
        requireNotNull(job).cancel()
    }
}

sealed class TaskState {
    data object Idle : TaskState()
    data object Running : TaskState()
    data class Error(val message: String) : TaskState()
    data object Success : TaskState()
}
