package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.mutableStateListOf
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
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
    override fun take(event: Event) {}

    override val selectedDeletes = mutableStateListOf<Int>()
}
