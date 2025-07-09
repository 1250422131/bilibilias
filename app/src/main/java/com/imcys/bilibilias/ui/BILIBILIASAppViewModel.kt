package com.imcys.bilibilias.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.AsCookiesStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class BILIBILIASAppViewModel(
    private val usersDataSource: UsersDataSource,
    private val userInfoRepository: UserInfoRepository,
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val asCookiesStorage: AsCookiesStorage
) : ViewModel() {

    sealed class UIState {
        data object Default : UIState()
        data class AccountCheck(
            val isCheckLoading: Boolean = false,
            val newCurrentUser: BILIUsersEntity? = null
        ) : UIState() // 正在检测
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Default)
    val uiState = _uiState.asStateFlow()

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

            userList.forEach loginCheck@{
                usersDataSource.setUserId(it.id)
                asCookiesStorage.syncDataBaseCookies()
                val loginInfo =
                    qrCodeLoginRepository.getLoginUserInfo(it.loginPlatform).lastOrNull()
                loginInfo?.let { info ->
                    if (info.status == ApiStatus.SUCCESS) {
                        _uiState.emit(UIState.Default)
                        return@launch
                    } else {
                        usersDataSource.setUserId(0)
                    }
                }
            }

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
}