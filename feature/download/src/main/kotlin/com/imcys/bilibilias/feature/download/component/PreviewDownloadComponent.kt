package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.mutableStateListOf
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDownloadComponent : DownloadComponent {
    private val testList = persistentListOf(
        DownloadTaskEntity.createTestDownloadTaskEntity(),
        DownloadTaskEntity.createTestDownloadTaskEntity(),
        DownloadTaskEntity.createTestDownloadTaskEntity(),
        DownloadTaskEntity.createTestDownloadTaskEntity(),
    )
    override val models: StateFlow<Model> = MutableStateFlow(
        Model(persistentListOf(testList), true),
    )

    override val dialogSlot: Value<ChildSlot<Unit, DialogComponent>> = MutableValue(ChildSlot())

    override fun take(event: Event) {}

    override val selectedDeletes = mutableStateListOf<Int>()
}
