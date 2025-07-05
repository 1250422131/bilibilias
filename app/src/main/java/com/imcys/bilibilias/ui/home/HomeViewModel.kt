package com.imcys.bilibilias.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val usersDataSource: UsersDataSource
) : ViewModel() {

    data class UIState(
        val fromLoginEventConsumed: Boolean = false
    )

    private var _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    private val _loginUserInfoState =
        MutableStateFlow<NetWorkResult<BILILoginUserModel?>>(emptyNetWorkResult())
    val loginUserInfoState = _loginUserInfoState.asStateFlow()

    private val _userLoginPlatformList =
        MutableStateFlow<List<BILIUsersEntity>>(emptyList())
    val userLoginPlatformList = _userLoginPlatformList.asStateFlow()


    init {
        showBILIUserInfo()
    }

    fun showBILIUserInfo() {
        viewModelScope.launch {
            initCurrentUserInfo()
        }
    }


    fun onNavigatedFromLogin() {
        viewModelScope.launch {
            _uiState.emit(uiState.value.copy(fromLoginEventConsumed = true))
        }
    }

    suspend fun initCurrentUserInfo() {
        if (!usersDataSource.isLogin()) return
        val biliUserList =
            qrCodeLoginRepository.getBILIUserListByUid(usersDataSource.getUserId())
        if (biliUserList.isEmpty()) return

        val currentUser = biliUserList.first { it.id == usersDataSource.getUserId() }

        qrCodeLoginRepository.getLoginUserInfo(
            currentUser.loginPlatform,
            currentUser.accessToken
        )
            .collect {
                _loginUserInfoState.emit(it)

                // 更新本地记录
                when (it) {
                    is NetWorkResult.Success<*> -> {
                        biliUserList.forEach { user ->
                            qrCodeLoginRepository.updateBILIUser(
                                user.copy(
                                    mid = it.data?.mid ?: 0L,
                                    name = it.data?.name ?: "",
                                    face = it.data?.face ?: "",
                                    level = it.data?.level ?: 0
                                )
                            )
                            _userLoginPlatformList.emit(biliUserList)
                        }
                    }

                    else -> {}
                }
            }


    }


}