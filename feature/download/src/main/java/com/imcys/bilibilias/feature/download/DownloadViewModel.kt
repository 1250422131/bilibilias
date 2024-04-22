package com.imcys.bilibilias.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.download.FileDownload
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val fileDownload: FileDownload,
) : ViewModel() {
    val taskFlow = fileDownload.taskFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, persistentListOf())

    fun onCancle(task: AsDownloadTask) {
        fileDownload.cancle(task)
    }
}
