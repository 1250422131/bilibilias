package com.imbys.bilibilias.feature.authorspace

import com.arkivanov.decompose.ComponentContext

interface AuthorSpaceComponent {
//    val models: StateFlow<Model>
//    val dialogSlot: Value<ChildSlot<*, DialogComponent>>

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): AuthorSpaceComponent
    }
}
