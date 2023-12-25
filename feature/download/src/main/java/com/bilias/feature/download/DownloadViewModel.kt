package com.bilias.feature.download

import android.content.Context
import androidx.lifecycle.ViewModel
import com.imcys.network.download.IDownloadManage
import com.imcys.network.download.downloadDir
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadManager: IDownloadManage
) : ViewModel() {

    fun taskList(context: Context) {
        val allTask = downloadManager.getAllTask(context.downloadDir.absolutePath)
    }
}
