package com.imcys.bilibilias.ui.setting.roam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.protobuf.copy
import com.imcys.bilibilias.BuildConfig
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.model.app.AppOldApplyRoamBean
import com.imcys.bilibilias.network.service.AppAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch


sealed interface RoamUIState {
    object Loading : RoamUIState
    data class Success(
        val isLoginTV: Boolean = false,
        val isLogin: Boolean = false,
    ) : RoamUIState

    data class Apply(
        val applyRoamBean: AppOldApplyRoamBean
    ) : RoamUIState

    data class Error(val errorMsg: String) : RoamUIState

    object NoLogin : RoamUIState
}

class RoamViewModel(
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao,
    private val appSettingsRepository: AppSettingsRepository,
    private val appAPIService: AppAPIService,
    private val userInfoRepository: UserInfoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<RoamUIState>(RoamUIState.Loading)
    val uiState = _uiState.asStateFlow()

    val appSettings = appSettingsRepository.appSettingsFlow


    fun updateLoginTVStatus() {
        viewModelScope.launch {
            if (uiState.value is RoamUIState.Success) {
                loadLoginState()
            }
        }
    }

    suspend fun loadLoginState() {
        val tvUser = biliUsersDao.getBILIUserByPlatform(LoginPlatform.TV)

        if (!userInfoRepository.isLogin() && !BuildConfig.ENABLED_PLAY_APP_MODE) {
            _uiState.emit(RoamUIState.NoLogin)
            return
        }

        _uiState.emit(
            RoamUIState.Success(
                isLoginTV = tvUser != null,
                isLogin = userInfoRepository.isLogin(),
            )
        )
    }

    fun updateRoamEnabledState(isEnabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateRoamEnabledState(isEnabled)
        }
    }

    fun loadRoamUIState() {
        viewModelScope.launch(Dispatchers.IO) {


            if (BuildConfig.ENABLED_PLAY_APP_MODE) {
                updateLoginTVStatus()
                return@launch
            }

            if (!userInfoRepository.isLogin()) {
                _uiState.emit(RoamUIState.NoLogin)
                return@launch
            }

            val mid = userInfoRepository.getCurrentUser()?.mid ?: 0L
            // 检测
            val applyRoamResult = appAPIService.checkApplyRoam(mid = mid)
            val applyRoamBean = applyRoamResult.getOrNull()
            if (applyRoamBean == null || applyRoamResult.isFailure) {
                _uiState.emit(
                    RoamUIState.Error(
                        applyRoamResult.exceptionOrNull()?.message ?: "未知错误"
                    )
                )
                return@launch
            }

            when (applyRoamBean.code) {
                0 -> {
                    val tvUser = biliUsersDao.getBILIUserByPlatform(LoginPlatform.TV)
                    _uiState.emit(
                        RoamUIState.Success(
                            isLoginTV = tvUser != null,
                            isLogin = userInfoRepository.isLogin(),
                        )
                    )
                }

                else -> {
                    _uiState.emit(RoamUIState.Apply(applyRoamBean))
                }
            }


        }
    }


    suspend fun applyRoam(reason: String): Result<AppOldApplyRoamBean> {
        val mid = userInfoRepository.getCurrentUser()?.mid ?: 0L
        return appAPIService.applyRoam(mid, reason)
    }

}