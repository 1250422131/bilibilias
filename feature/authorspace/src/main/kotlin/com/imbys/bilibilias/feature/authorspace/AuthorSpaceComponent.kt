package com.imbys.bilibilias.feature.authorspace

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Mid
import kotlinx.coroutines.flow.StateFlow

interface AuthorSpaceComponent {
    val models: StateFlow<Model>

    data class Model(val units: List<String>)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): AuthorSpaceComponent
    }
}
