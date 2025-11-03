package com.imcys.bilibilias.ui.setting.contract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.datastore.AppSettingsSerializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class NamingConventionViewModel(
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    val appSettings = appSettingsRepository.appSettingsFlow

    fun updateVideoNamingRule(rule: String) {
        viewModelScope.launch {
            appSettingsRepository.updateVideoNamingRule(rule)
        }
    }

    fun updateDonghuaNamingRule(rule: String) {
        viewModelScope.launch {
            appSettingsRepository.updateBangumiNamingRule(rule)
        }
    }
}