package com.imcys.bilibilias.feature.download.component

import androidx.compose.runtime.mutableStateListOf
import androidx.core.net.toUri
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.imcys.bilibilias.core.database.model.DownloadTaskEntity
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo
import com.imcys.bilibilias.feature.download.sheet.DialogComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDownloadComponent : DownloadComponent {
    private val testList = persistentListOf(
        DownloadTaskEntity(
            "0".toUri(),
            0,
            "0",
            0,
            FileType.AUDIO,
            "subtitle",
            "title",
            id = 0
        ),
        DownloadTaskEntity(
            "1".toUri(),
            1,
            "1",
            1,
            FileType.AUDIO,
            "subtitle",
            "title",
            id = 1
        ),
        DownloadTaskEntity(
            "2".toUri(),
            2,
            "2",
            2,
            FileType.AUDIO,
            "subtitle",
            "title",
            id = 2
        ),
        DownloadTaskEntity(
            "3".toUri(),
            3,
            "3",
            3,
            FileType.VIDEO,
            "subtitle",
            "title",
            id = 3
        )
    )
    override val models: StateFlow<Model> = MutableStateFlow(
        Model(persistentListOf(testList))
    )
    override val tasks: StateFlow<ImmutableList<ImmutableList<DownloadTaskEntity>>> =
        MutableStateFlow(persistentListOf(testList))

    override val dialogSlot: Value<ChildSlot<Unit, DialogComponent>> = MutableValue(ChildSlot())

    override fun take(event: Event) {}

    override fun onSettingsClicked(info: ViewInfo, fileType: FileType) {}
    override fun onSelected(id: Int) {
        TODO("Not yet implemented")
    }

    override val selectedDeletes = mutableStateListOf<Int>()


}
