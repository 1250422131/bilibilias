package com.imcys.bilibilias.feature.tool.download

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.tool.download.DownloadBottomSheetComponent.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DefaultDownloadBottomSheetComponent(componentContext: ComponentContext) :
    DownloadBottomSheetComponent, ComponentContext by componentContext {
    override val model: StateFlow<Model> = MutableStateFlow(Model)
}
