package com.imcys.bilibilias.ui.download

import androidx.lifecycle.ViewModel
import com.imcys.bilibilias.dwonload.DownloadManager

class DownloadViewModel(
    private val downloadManager: DownloadManager
) : ViewModel() {

    val downloadListState = downloadManager.getAllDownloadTasks()


}