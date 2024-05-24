package com.imcys.bilibilias.feature.home

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.IComponentContext

interface HomeComponent  {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): HomeComponent
    }
}
