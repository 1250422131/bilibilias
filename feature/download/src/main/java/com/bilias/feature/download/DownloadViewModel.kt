package com.bilias.feature.download

import android.content.Context
import androidx.lifecycle.ViewModel
import com.imcys.model.download.Entry
import com.imcys.network.download.IDownloadManage
import com.imcys.network.download.downloadDir
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadManager: IDownloadManage
) : ViewModel() {
    private val _taskState = MutableStateFlow(TaskState())
    val taskState = _taskState.asStateFlow()

    fun taskList(context: Context) {
        val allTask = downloadManager.getAllTask(context.downloadDir.absolutePath)
            .map(Entry::mapToTask)
            .toImmutableList()
        _taskState.update { it.copy(taskList = allTask) }
    }
}
