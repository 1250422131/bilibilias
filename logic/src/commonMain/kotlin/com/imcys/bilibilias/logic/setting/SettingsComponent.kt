package com.imcys.bilibilias.logic.setting

import com.imcys.bilibilias.logic.root.AppComponentContext

interface SettingsComponent {
}

class DefaultSettingsComponent(
    componentContext: AppComponentContext
) : SettingsComponent, AppComponentContext by componentContext {

}