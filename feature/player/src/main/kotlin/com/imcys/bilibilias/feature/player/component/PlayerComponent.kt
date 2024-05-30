package com.imcys.bilibilias.feature.player.component

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import kotlinx.coroutines.flow.StateFlow

interface PlayerComponent {
    val models: StateFlow<Model>

    data class Model(val uris: List<Uri>)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            viewInfo: ViewInfo,
            fileType: FileType,
        ): PlayerComponent
    }
}
