package com.imcys.bilibilias.ui.setting.roam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.source.UsersDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class RoamViewModel(
    private val usersDataSource: UsersDataSource,
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    data class UIState(
        val isLoginTV: Boolean = false,
        val isLogin: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    val appSettings = appSettingsRepository.appSettingsFlow

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLogin = usersDataSource.isLogin(),
            )
        }
    }


    fun updateLoginTVStatus() {
        viewModelScope.launch {
            val tvUser = biliUsersDao.getBILIUserByPlatform(LoginPlatform.TV)
            _uiState.value = _uiState.value.copy(
                isLoginTV = tvUser != null
            )
        }
    }

    fun updateRoamEnabledState(isEnabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateRoamEnabledState(isEnabled)
        }
    }


}