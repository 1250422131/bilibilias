package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.molecule.RecompositionMode
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.common.utils.addOrRemove
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

class DefaultDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val dialogComponentFactory: DialogComponent.Factory,
    private val taskDao: DownloadTaskDao,
    private val downloadManager: DownloadManager,
) : DownloadComponent, BaseViewModel<Event, Model>(componentContext) {
    override val recompositionMode = RecompositionMode.Immediate

    override val selectedDeletes = mutableStateListOf<Int>()

    private val dialogNavigation = SlotNavigation<BottomConfig>()

    override val dialogSlot: Value<ChildSlot<*, DialogComponent>> =
        childSlot(
            source = dialogNavigation,
            serializer = BottomConfig.serializer(),
            handleBackButton = true,
        ) { config, childComponentContext ->
            dialogComponentFactory(
                childComponentContext,
                config.info,
                config.fileType,
                dialogNavigation::dismiss,
            )
        }

    @Composable
    override fun models(events: Flow<Event>): Model {
        return PresentationLogic(
            events,
            taskDao.findAllTaskByGroupCid()
        )
    }

    @Composable
    private fun PresentationLogic(
        events: Flow<Event>,
        findAllTaskByGroupCid: Flow<Map<Cid, List<DownloadTaskEntity>>>,
    ): Model {
        val state by findAllTaskByGroupCid.map {
            Napier.d { it.values.joinToString("\n") }
            it.values.map { it.toImmutableList() }.toImmutableList()
        }.collectAsState(initial = persistentListOf())

        var canDelete by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    is Event.UserSelecte -> selectedDeletes.addOrRemove(event.id)

                    Event.ConfirmDeletion -> {
                        downloadManager.delete(selectedDeletes)
                        canDelete = false
                    }

                    Event.OpenDeleteOption -> {
                        canDelete = true
                        selectedDeletes.clear()
                    }
                    Event.CloseDeleteOption -> canDelete = false
                }
            }
        }

        return Model(state, canDelete)
    }

    private fun showDialog(info: ViewInfo, fileType: FileType) {
        dialogNavigation.activate(BottomConfig(info, fileType))
    }

    @Serializable
    private data class BottomConfig(val info: ViewInfo, val fileType: FileType)

    @Serializable
    sealed interface Config {
        data object Download : Config

        @Serializable
        data class Player(val info: ViewInfo, val fileType: FileType) : Config
    }

    @AssistedFactory
    interface Factory : DownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDownloadComponent
    }
}
