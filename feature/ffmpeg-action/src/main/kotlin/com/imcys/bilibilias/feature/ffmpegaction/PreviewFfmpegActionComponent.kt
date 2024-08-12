package com.imcys.bilibilias.feature.ffmpegaction

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PreviewFfmpegActionComponent : FfmpegActionComponent {
    override val models: StateFlow<Unit>
        get() = MutableStateFlow(Unit)

    override fun take(event: Unit) {

    }
}
