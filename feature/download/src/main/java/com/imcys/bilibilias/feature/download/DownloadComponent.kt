package com.imcys.bilibilias.feature.download

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.ViewInfo
import kotlinx.coroutines.flow.StateFlow

interface DownloadComponent {
    val taskFlow: StateFlow<Map<Cid, List<DownloadTask>>>

    suspend fun getPlayerInfo(aid: Aid, bvid: Bvid, cid: Cid): List<DownloadTaskEntity>

    fun onDelete(viewInfo: ViewInfo, fileType: FileType)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DownloadComponent
    }
}
