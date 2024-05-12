package com.imcys.bilibilias.feature.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.ViewInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.async
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
        .map { it.groupBy { it.viewInfo.cid } }
        .stateIn(viewModelScope, SharingStarted.Lazily, persistentMapOf())

    suspend fun getPlayerInfo(aid: Aid, bvid: Bvid, cid: Cid): List<DownloadTaskEntity> {
        return downloadTaskDao.getTaskByInfo(aid, bvid, cid)
    }

    fun onDelete(viewInfo: ViewInfo, fileType: FileType) {
        downloadManager.delete(viewInfo, fileType)
    }
}
