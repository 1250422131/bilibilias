package com.imcys.bilibilias.feature.download

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.download.sheet.DefaultDialogComponent
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

class DefaultDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val downloadManager: DownloadManager,
    private val downloadTaskDao: DownloadTaskDao,
) : DownloadComponent, BaseViewModel<Event, Model>(componentContext) {

    private val dialogNavigation = SlotNavigation<BottomConfig>()

    override val dialogSlot: Value<ChildSlot<*, DialogComponent>> =
        childSlot(
            source = dialogNavigation,
            serializer = null,
            handleBackButton = true,
        ) { config, childComponentContext ->
            DefaultDialogComponent(
                componentContext = childComponentContext,
                onDismissed = dialogNavigation::dismiss,
            )
        }

    override fun onSettingsClicked() {
        showDialog()
    }

    private fun showDialog() {
        dialogNavigation.activate(BottomConfig)
    }

    @Composable
    override fun models(events: Flow<Event>): Model {
        return PresentationLogic(
            events,
            downloadManager,
            downloadTaskDao.loadAllDownloadFlow()
        )
    }

    @Composable
    private fun PresentationLogic(
        events: Flow<Event>,
        downloadManager: DownloadManager,
        loadAllDownloadFlow: Flow<List<DownloadTaskEntity>>
    ): Model {
        val entities by loadAllDownloadFlow.collectAsState(initial = emptyList())

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is Event.DeleteFile -> downloadManager.delete(event.viewInfo, event.fileType)
                }
            }
        }

        return Model(
            entities.groupBy { it.cid }
                .values
                .map {
                    Napier.d { it.joinToString("\n") }
                    it.map(DownloadTaskEntity::mapToTask)
                }
                .toImmutableList()
        )
    }

    @Serializable
    private data object BottomConfig

    @AssistedFactory
    interface Factory : DownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDownloadComponent
    }
}
