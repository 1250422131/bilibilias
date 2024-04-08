package com.imcys.bilibilias.feature.download

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.network.download.FileDownload
import com.imcys.bilibilias.core.network.download.FileType
import com.imcys.bilibilias.core.network.download.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val fileDownload: FileDownload,
) : ViewModel() {
    val k = fileDownload.allTaskFlow().map {
        Napier.d { it.toString() }
        it.map(Task::mapToTaskState)
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun cancel(state: TaskState) {
        fileDownload.cancel(state.type, state.bvid, state.cid)
    }
}

sealed interface DownloadUiState {
    data class Success(val name: String) : DownloadUiState
}

fun Task.mapToTaskState(): TaskState {
    return TaskState(title, type, groupTag, groupId).apply {
        isRunning = this@mapToTaskState.isRunning
        progress = this@mapToTaskState.progress
    }
}

data class TaskState(val title: String, val type: FileType, val bvid: String, val cid: Long) {
    var isRunning by mutableStateOf(false)
    var progress by mutableFloatStateOf(0f)
}
