package com.imcys.bilibilias.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.AsCookiesStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class SettingViewModel(
    private val appSettingsRepository: AppSettingsRepository,
    private val usersDataSource: UsersDataSource,
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao,
    private val userInfoRepository: UserInfoRepository,
    private val asCookiesStorage: AsCookiesStorage
) : ViewModel() {

    val appSettings = appSettingsRepository.appSettingsFlow

    private val _uiState = MutableStateFlow(SettingUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            usersDataSource.users.collect {
                var mid = 0L
                if (it.currentUserId != 0L) {
                    val user = biliUsersDao.getBILIUserByUid(it.currentUserId)
                    mid = user?.mid ?: 0L
                }
                _uiState.value = _uiState.value.copy(
                    isLogin = it.currentUserId != 0L,
                    currentMid = mid
                )
            }
        }
    }

    fun updatePrivacyPolicyAgreement(agreed: AppSettings.AgreePrivacyPolicyState) {
        viewModelScope.launch {
            appSettingsRepository.updatePrivacyPolicyAgreement(agreed)
            Firebase.app.isDataCollectionDefaultEnabled =
                agreed == AppSettings.AgreePrivacyPolicyState.Agreed
        }
    }

    fun updateEnabledDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateEnabledDynamicColor(enabled)
        }
    }

    fun updateClipboardAutoHandling(enabled: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.updateClipboardAutoHandling(enabled)
        }
    }

    suspend fun logout() {
        val user = biliUsersDao.getBILIUserByUid(usersDataSource.getUserId())
        user?.let {
            val cookies = biliUserCookiesDao.getBILIUserCookiesByUid(it.id)

            val biliJct =
                cookies.firstOrNull { cookie -> cookie.name == "bili_jct" }?.value?.apply {
                    usersDataSource.setUserId(0L)
                    biliUserCookiesDao.deleteBILICookiesByUid(user.id)
                    biliUsersDao.deleteBILIUserByUid(user.id)
                } ?: return

            _uiState.value = _uiState.value.copy(
                isLogin = false,
                currentMid = 0
            )

            userInfoRepository.logout(biliJct).last()
            asCookiesStorage.clearCookies()
            asCookiesStorage.syncDataBaseCookies()
        }
    }

}