package com.imcys.bilibilias.feature.settings.component

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.feature.settings.UserEditEvent
import com.imcys.bilibilias.feature.settings.UserEditableSettings
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
