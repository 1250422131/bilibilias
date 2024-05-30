package com.imcys.bilibilias.feature.player.component

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultPlayerComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val viewInfo: ViewInfo,
    @Assisted private val fileType: FileType,
    private val downloadTaskDao: DownloadTaskDao,
) : PlayerComponent, BaseViewModel<Unit, PlayerComponent.Model>(componentContext) {

    @Composable
    override fun models(events: Flow<Unit>): PlayerComponent.Model {
        val uris = remember { mutableStateListOf<Uri>() }
        LaunchedEffect(Unit) {
            downloadTaskDao.getTaskByInfo(viewInfo.aid, viewInfo.bvid, viewInfo.cid).map {
                uris += it.uri
            }
        }

        return PlayerComponent.Model(uris)
    }

    @AssistedFactory
    interface Factory : PlayerComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            viewInfo: ViewInfo,
            fileType: FileType,
        ): DefaultPlayerComponent
    }
}
