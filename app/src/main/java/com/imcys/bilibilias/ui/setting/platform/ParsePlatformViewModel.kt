package com.imcys.bilibilias.ui.setting.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.data.repository.toDatabaseType
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.AsCookiesStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ParsePlatformViewModel(
    private val usersDataSource: UsersDataSource,
    private val appSettingsRepository: AppSettingsRepository,
    private val userInfoRepository: UserInfoRepository,
    private val asCookiesStorage: AsCookiesStorage,
    private val qrCodeLoginRepository: QRCodeLoginRepository,
) : ViewModel() {

    val appSettings = appSettingsRepository.appSettingsFlow

    val useParsePlatform: StateFlow<AppSettings.VideoParsePlatform>
        field = MutableStateFlow<AppSettings.VideoParsePlatform>(AppSettings.VideoParsePlatform.Web)


    sealed interface ParsePlatformUIState {
        data object Default : ParsePlatformUIState

        data object ChangeLoading : ParsePlatformUIState

        data object EffectiveCheckLoading : ParsePlatformUIState
        data class AccountSelect(
            val accountList: List<BILIUsersEntity> = emptyList()
        ) : ParsePlatformUIState
    }


    init {
        viewModelScope.launch {
            appSettings.collect { settings ->
                useParsePlatform.value = settings.videoParsePlatform
            }
        }
    }

    val uiState: StateFlow<ParsePlatformUIState>
        field = MutableStateFlow<ParsePlatformUIState>(ParsePlatformUIState.Default)


    fun updateSelectPlatform(platform: AppSettings.VideoParsePlatform) {
        viewModelScope.launch {
            usersDataSource.setUserId(0)
            uiState.value = ParsePlatformUIState.ChangeLoading
            appSettingsRepository.updateVideoParsePlatform(platform)
            checkLocalAccounts()
        }
    }


    fun selectAccount(biliUsersEntity: BILIUsersEntity) {
        viewModelScope.launch {
            uiState.value = ParsePlatformUIState.EffectiveCheckLoading
            if (!checkAccountEffective(biliUsersEntity)){
                checkLocalAccounts()
            } else {
                uiState.value = ParsePlatformUIState.Default
            }
        }
    }

    suspend fun checkLocalAccounts() {
        val userList = userInfoRepository.getBILIUserListByPlatform(
            appSettingsRepository.appSettingsFlow.first().videoParsePlatform.toDatabaseType()
        )
        if (userList.size == 1) {
            checkAccountEffective(userList.first())
            uiState.value = ParsePlatformUIState.Default
        } else if (userList.isNotEmpty()){
            uiState.value = ParsePlatformUIState.AccountSelect(userList)
        } else {
            uiState.value = ParsePlatformUIState.Default
        }
    }

    suspend fun checkAccountEffective(biliUsersEntity: BILIUsersEntity): Boolean {
        val oldUid = biliUsersEntity.id
        usersDataSource.setUserId(biliUsersEntity.id)
        asCookiesStorage.syncDataBaseCookies()
        val loginInfo =
            qrCodeLoginRepository.checkLoginUserInfo(
                biliUsersEntity.loginPlatform,
                biliUsersEntity.accessToken
            )
        val isSuccess = loginInfo.isSuccess
        if (!isSuccess) {
            usersDataSource.setUserId(0)
            userInfoRepository.deleteBILIUserByUid(oldUid)
            asCookiesStorage.syncDataBaseCookies()
        }
        return isSuccess
    }
}