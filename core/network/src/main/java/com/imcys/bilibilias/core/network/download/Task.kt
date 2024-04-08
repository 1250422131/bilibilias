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
    var errorMessage by mutableStateOf<String?>(null)
    internal var job: Job? = null
    var isRunning by mutableStateOf(false)
        internal set
    val isSuccess = errorMessage == null

    val isFailed = errorMessage != null

    var progress by mutableFloatStateOf(0f)
        internal set

    fun cancelTask() {
        requireNotNull(job).cancel()
    }
}
