package com.imcys.bilibilias.feature.download.component

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
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.dao.DownloadTaskDao
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import com.imcys.bilibilias.feature.player.PlayerComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

class DefaultDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val downloadTaskDao: DownloadTaskDao,
    private val dialogComponentFactory: DialogComponent.Factory,
    private val playerComponentFactory: PlayerComponent.Factory
) : DownloadComponent, BaseViewModel<Event, Model>(componentContext) {
    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, DownloadComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Download,
            handleBackButton = true,
            childFactory = ::child,
        )

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
                onNavigationToPlayer = ::onPlayerClicked
            )
        }

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): DownloadComponent.Child = when (config) {
        Config.Download -> DownloadComponent.Child.DownloadChild
        is Config.Player -> DownloadComponent.Child.PlayerChild(
            playerComponentFactory(
                componentContext
            )
        )
    }

    override fun onSettingsClicked(info: ViewInfo, fileType: FileType) {
        showDialog(info, fileType)
    }

    override fun onPlayerClicked() {
        navigation.push(Config.Player(""))
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
        val entities by loadAllDownloadFlow.collectAsState(initial = emptyList())

        LaunchedEffect(Unit) {
            events.collect { event ->
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

    private fun showDialog(info: ViewInfo, fileType: FileType) {
        dialogNavigation.activate(BottomConfig(info, fileType))
    }

    @Serializable
    private data class BottomConfig(val info: ViewInfo, val fileType: FileType)

    @Serializable
    sealed interface Config {
        data object Download : Config

        @Serializable
        data class Player(val name: String) : Config
    }

    @AssistedFactory
    interface Factory : DownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultDownloadComponent
    }
}
