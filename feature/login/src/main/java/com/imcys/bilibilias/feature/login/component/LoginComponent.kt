package com.imcys.bilibilias.feature.login.component

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.login.LoginEvent
import com.imcys.bilibilias.feature.login.LoginModel
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {
    val models: StateFlow<LoginModel>
    fun take(event: LoginEvent)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): LoginComponent
    }
}
