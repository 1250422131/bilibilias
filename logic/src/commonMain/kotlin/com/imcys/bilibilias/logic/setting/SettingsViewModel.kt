package com.imcys.bilibilias.logic.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.model.Codecs
import com.imcys.bilibilias.core.datastore.model.UserPreferences
import com.imcys.bilibilias.logic.stateInViewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val asPreferencesDataSource: AsPreferencesDataSource,
) : ViewModel() {
    val preferences = asPreferencesDataSource.userData
        .stateInViewModelScope(UserPreferences.DEFAULT)

    fun setTryLook(enable: Boolean) {
        viewModelScope.launch {
            asPreferencesDataSource.setTryLookEnabled(enable)
        }
    }

    fun setDecoderCodecPriorityList(newCodecs: List<Codecs>) {
        viewModelScope.launch {
            asPreferencesDataSource.setDecoderCodecPriorityList(newCodecs)
        }
    }
}