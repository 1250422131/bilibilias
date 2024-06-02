package com.imcys.bilibilias.feature.settings

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SettingsComponent {
    val models: StateFlow<UserEditableSettings>
    fun take(event: UserEditEvent)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): SettingsComponent
    }
}
