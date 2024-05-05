package com.imcys.bilibilias.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.core.datastore.preferences.AsPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val asPreferencesDataSource: AsPreferencesDataSource
) : ViewModel() {
    val settingsUiState: StateFlow<SettingsUiState> = asPreferencesDataSource.userData.map {
        SettingsUiState.Success(
            settings = UserEditableSettings(
                fileStoragePath = it.fileStoragePath,
                folderNameRule = it.fileNameRule,
                autoMerge = it.autoMerge,
                autoImport = it.autoImport,
                shouldAppcenter = it.shouldAppcenter,
                command = it.command
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = SettingsUiState.Loading,
    )

    fun updateFileStoragePath(path: String?) {
        viewModelScope.launch {
            asPreferencesDataSource.setFileStoragePath(path ?: "")
        }
    }

    fun updateFileNameRule(rule: String) {
        viewModelScope.launch {
            asPreferencesDataSource.setFileNameRule(rule)
        }
    }

    fun updateAutoMerge(useAutoMerge: Boolean) {
        viewModelScope.launch {
            asPreferencesDataSource.setAutoMerge(useAutoMerge)
        }
    }

    fun updateAutoImportToBilibili(useAutoImport: Boolean) {
        viewModelScope.launch {
            asPreferencesDataSource.setAutoImportToBilibili(useAutoImport)
        }
    }

    fun updateShouldAppcenter(useAppcenter: Boolean) {
        viewModelScope.launch {
            asPreferencesDataSource.setShouldAppcenter(useAppcenter)
        }
    }

    fun updateMergeCommand(command: String) {
        viewModelScope.launch {
            asPreferencesDataSource.setCommand(command)
        }
    }
}

data class UserEditableSettings(
    val fileStoragePath: String,
    val folderNameRule: String,
    val autoMerge: Boolean,
    val autoImport: Boolean,
    val command: String,
    val shouldAppcenter: Boolean,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}
