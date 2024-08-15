package com.imcys.bilibilias.feature.tool.download

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

class DefaultDownloadBottomSheetComponent(componentContext: ComponentContext) :
    DownloadBottomSheetComponent, ComponentContext by componentContext {
    override val model: StateFlow<DownloadBottomSheetComponent.Model> = TODO()
}
