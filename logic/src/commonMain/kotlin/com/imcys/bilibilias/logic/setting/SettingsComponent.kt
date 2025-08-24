package com.imcys.bilibilias.logic.setting

import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface SettingsComponent {
    val preferences: StateFlow<UserPreferences>
    fun setTryLook(enable: Boolean)
}

class DefaultSettingsComponent(
    componentContext: AppComponentContext,
    private val asPreferencesDataSource: AsPreferencesDataSource,
) : SettingsComponent, AppComponentContext by componentContext {
    override val preferences = asPreferencesDataSource.userData
        .stateInBackground(UserPreferences.DEFAULT)

    override fun setTryLook(enable: Boolean) {
        applicationScope.launch {
            asPreferencesDataSource.setTryLookEnabled(enable)
        }
    }
}