package com.imcys.bilibilias.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.download.task.AsDownloadTask
import com.imcys.bilibilias.core.model.video.ViewInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadManager: DownloadManager,
    private val downloadTaskDao: DownloadTaskDao,
) : ViewModel() {
    val taskFlow = downloadTaskDao.loadAllDownloadFlow()
        .map {
            Napier.d { it.joinToString("\n") }
            it.map {
                DownloadTask(
                    it.id,
                    ViewInfo(it.aid, it.bvid, it.cid, it.title),
                    it.subTitle,
                    it.fileType,
                    it.uri,
                    it.progress,
                    it.state.cn
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, persistentListOf())

    fun onCancle(task: AsDownloadTask) {
        downloadManager.cancle(task)
    }
}
