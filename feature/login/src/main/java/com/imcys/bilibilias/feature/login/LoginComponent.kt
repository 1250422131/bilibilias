package com.imcys.bilibilias.feature.login

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.common.IComponentContext

interface LoginComponent {
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): LoginComponent
    }
}
