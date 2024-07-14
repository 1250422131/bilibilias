package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.StateFlow

interface DownloadComponent {
    val models: StateFlow<Model>
    val dialogSlot: Value<ChildSlot<*, DialogComponent>>

    val selectedDeletes: SnapshotStateList<Int>

    fun take(event: Event)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): DownloadComponent
    }
}
