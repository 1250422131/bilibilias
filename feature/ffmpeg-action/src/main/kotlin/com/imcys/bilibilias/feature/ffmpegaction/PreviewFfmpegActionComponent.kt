package com.imcys.bilibilias.feature.ffmpegaction

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PreviewFfmpegActionComponent : FfmpegActionComponent {
    override val models: StateFlow<State>
        get() = MutableStateFlow(State("", "", {}))

    override fun take(event: Unit) {}
}
