package com.imcys.bilibilias.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.network.download.FileDownload
import com.imcys.bilibilias.core.network.download.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val fileDownload: FileDownload,
) : ViewModel() {
    val allTaskFlow = fileDownload.allTaskFlow().map {
        it.map(Task::mapToTaskState)
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun cancel(state: TaskState) {
        fileDownload.cancel(state.type, state.bvid, state.cid)
    }
}
