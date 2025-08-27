package com.imcys.bilibilias.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import kotlinx.coroutines.launch

class SettingViewModel(
    private val appSettingsRepository: AppSettingsRepository
): ViewModel() {

    val appSettings = appSettingsRepository.appSettingsFlow


    fun updateEnabledDynamicColor(enabled: Boolean){
        viewModelScope.launch {
            appSettingsRepository.updateEnabledDynamicColor(enabled)
        }
    }

}