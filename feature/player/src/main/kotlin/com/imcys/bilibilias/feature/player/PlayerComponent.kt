package com.imcys.bilibilias.feature.player

import com.arkivanov.decompose.ComponentContext

interface PlayerComponent {

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): PlayerComponent
    }
}
