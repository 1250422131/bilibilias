package com.imcys.bilibilias.feature.download

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val downloadManager: DownloadManager,
    private val downloadTaskDao: DownloadTaskDao,
) : DownloadComponent, BaseViewModel<Unit, Unit>(componentContext) {
    override val taskFlow = downloadTaskDao.loadAllDownloadFlow()
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

    override suspend fun getPlayerInfo(aid: Aid, bvid: Bvid, cid: Cid): List<DownloadTaskEntity> {
        return downloadTaskDao.getTaskByInfo(aid, bvid, cid)
    }

    override fun onDelete(viewInfo: ViewInfo, fileType: FileType) {
        downloadManager.delete(viewInfo, fileType)
    }

    @Composable
    override fun models(events: Flow<Unit>) {
        TODO("Not yet implemented")
    }

    @AssistedFactory
    interface Factory : DownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDownloadComponent
    }
}
