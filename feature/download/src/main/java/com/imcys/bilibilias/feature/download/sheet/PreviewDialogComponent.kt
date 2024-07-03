package com.imcys.bilibilias.feature.download.sheet

import com.imcys.bilibilias.core.model.video.ViewInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewDialogComponent : DialogComponent {
    override val models: StateFlow<Unit> = MutableStateFlow(Unit)
    override fun onDismissClicked() {}

    override fun navigationToPlayer(): ViewInfo = ViewInfo(0, "", 0, "")

    override fun take(event: DialogComponent.Event) {}
}
