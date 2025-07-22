package com.imcys.bilibilias.logic.setting

import com.arkivanov.decompose.ComponentContext

interface SettingsComponent {
}

class DefaultSettingsComponent(
    componentContext: ComponentContext
) : SettingsComponent, ComponentContext by componentContext {

}