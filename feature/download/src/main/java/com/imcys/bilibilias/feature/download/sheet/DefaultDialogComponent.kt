package com.imcys.bilibilias.feature.download.sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultDialogComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val viewInfo: ViewInfo,
    @Assisted private val fileType: FileType,
    @Assisted("dismissed") private val onDismissed: () -> Unit,
    @Assisted("navigation")
    private val onNavigationToPlayer: (info: ViewInfo, fileType: FileType) -> Unit,
    private val downloadManager: DownloadManager
) : DialogComponent, BaseViewModel<DialogComponent.Event, Unit>(componentContext) {
    @Composable
    override fun models(events: Flow<DialogComponent.Event>) {
        LaunchedEffect(Unit) {
            events.collect {
                when (it) {
                    is DialogComponent.Event.DeleteFile -> downloadManager.delete(
                        viewInfo,
                        fileType
                    )
                }
            }
        }
    }

    override fun onDismissClicked() {
        onDismissed()
    }

    override fun onNavigationToPlayer() {
        onNavigationToPlayer.invoke(viewInfo, fileType)
    }

    @AssistedFactory
    interface Factory : DialogComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            info: ViewInfo,
            fileType: FileType,
            @Assisted("dismissed") onDismissed: () -> Unit,
            @Assisted("navigation")
            onNavigationToPlayer: (info: ViewInfo, fileType: FileType) -> Unit,
        ): DefaultDialogComponent
    }
}
