package com.imcys.bilibilias.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.app
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.AsCookiesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BILIBILIASAppViewModel(
    private val usersDataSource: UsersDataSource,
    private val userInfoRepository: UserInfoRepository,
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val asCookiesStorage: AsCookiesStorage,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {
    val appSettings = appSettingsRepository.appSettingsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppSettings.getDefaultInstance()
    )
    private val _uiState = MutableStateFlow<UIState>(UIState.Default)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsRepository.appSettingsFlow.collect {
                if (it.knowAboutApp != AppSettings.KnowAboutApp.Know) {
                    _uiState.value = UIState.KnowAboutApp
                }
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

    fun accountLoginStateError() {

        viewModelScope.launch {
            if (!usersDataSource.isLogin()) return@launch
            _uiState.emit(UIState.AccountCheck(true))
            // 检测可用B站账户
            val oldCurrentUser = userInfoRepository.getBILIUserByUid(usersDataSource.getUserId())
            val oldMid = oldCurrentUser?.mid
            userInfoRepository.deleteBILIUserByUid(usersDataSource.getUserId())
            val userList = userInfoRepository.getBILIUserListByMid(oldMid ?: 0)
            if (userList.isEmpty()) {
                // 通知没找到合适的账户平台
                usersDataSource.setUserId(0)
                _uiState.emit(UIState.AccountCheck(false))
                delay(1500)
                _uiState.emit(UIState.Default)
                return@launch
            }

            var isResult = false
            userList.forEach loginCheck@{
                usersDataSource.setUserId(it.id)
                asCookiesStorage.syncDataBaseCookies()
                val loginInfo =
                    qrCodeLoginRepository.getLoginUserInfo(it.loginPlatform).lastOrNull()
                loginInfo?.let { info ->
                    if (info.status == ApiStatus.SUCCESS) {
                        if (!isResult) {
                            _uiState.emit(UIState.Default)
                        }
                        isResult = true
                    } else {
                        usersDataSource.setUserId(0)
                    }
                }
            }

            if (isResult) return@launch

            // 到达此处
            usersDataSource.setUserId(0)
            _uiState.emit(UIState.AccountCheck(false))
            delay(1500)
            _uiState.emit(UIState.Default)


        }
    }

    fun updateUIState(uiState: UIState) {
        viewModelScope.launch {
            _uiState.emit(uiState)
        }
    }


    fun onKnowAboutApp() {
        viewModelScope.launch(Dispatchers.IO) {
            appSettingsRepository.updateKnowAboutApp(AppSettings.KnowAboutApp.Know)
            _uiState.value = UIState.Default

        }
    }

    fun ontUseTVVoucherInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            usersDataSource.setNotUseBuvid3(true)
            _uiState.value = UIState.Default
        }
    }
}