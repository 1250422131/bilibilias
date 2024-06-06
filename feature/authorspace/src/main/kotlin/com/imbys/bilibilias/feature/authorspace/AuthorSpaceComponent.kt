package com.imbys.bilibilias.feature.authorspace

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Mid

interface AuthorSpaceComponent {
//    val models: StateFlow<Model>
//    val dialogSlot: Value<ChildSlot<*, DialogComponent>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): AuthorSpaceComponent
    }
}
