package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.launchMolecule
import app.cash.molecule.moleculeFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

class DefaultDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val downloadTaskDao: DownloadTaskDao,
    private val dialogComponentFactory: DialogComponent.Factory,
) : DownloadComponent, BaseViewModel<Event, Model>(componentContext) {

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

    override fun onSettingsClicked(info: ViewInfo, fileType: FileType) {
        showDialog(info, fileType)
    }

    @Composable
    override fun models(events: Flow<Event>): Model {
        return PresentationLogic(
            events,
            downloadTaskDao.loadAllDownloadFlow()
        )
    }

    @Composable
    private fun PresentationLogic(
        events: Flow<Event>,
        loadAllDownloadFlow: Flow<List<DownloadTaskEntity>>
    ): Model {
        val entities = remember { loadAllDownloadFlow }.collectAsState(initial = emptyList())

        LaunchedEffect(Unit) {
            events.collect { event ->
            }
        }

        return Model(
            entities.value.groupBy { it.cid }
                .values
                .map {
                    Napier.d { it.joinToString("\n") }
                    it.map(DownloadTaskEntity::mapToTask)
                }
                .toImmutableList()
        )
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
        data class Player(
            val info: ViewInfo,
            val fileType: FileType,
        ) : Config
    }

    @AssistedFactory
    interface Factory : DownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDownloadComponent
    }
}
