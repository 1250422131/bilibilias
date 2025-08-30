package com.imcys.bilibilias.ui.home

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.data.repository.AppSettingsRepository
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.RiskManagementRepository
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.dwonload.DownloadManager
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val usersDataSource: UsersDataSource,
    private val riskManagementRepository: RiskManagementRepository,
    private val downloadManager: DownloadManager,
    private val appSettingsRepository: AppSettingsRepository
) : ViewModel() {

    data class UIState(
        val fromLoginEventConsumed: Boolean = false
    )
    val appSettings = appSettingsRepository.appSettingsFlow

    private var _uiState = MutableStateFlow(UIState())
    val uiState = _uiState.asStateFlow()

    private val _loginUserInfoState =
        MutableStateFlow<NetWorkResult<BILILoginUserModel?>>(emptyNetWorkResult())
    val loginUserInfoState = _loginUserInfoState.asStateFlow()

    private val _userLoginPlatformList =
        MutableStateFlow<List<BILIUsersEntity>>(emptyList())
    val userLoginPlatformList = _userLoginPlatformList.asStateFlow()

    val downloadListState = downloadManager.getAllDownloadTasks()


    private val _homeLayoutTypesetList = MutableStateFlow(emptyList<AppSettings.HomeLayoutItem>())

    var homeLayoutTypesetList = _homeLayoutTypesetList.asStateFlow()

    init {
        initLayoutTypeset()
        showBILIUserInfo()
        initDownloadList()
    }

    private fun initLayoutTypeset() {
        viewModelScope.launch {
            appSettingsRepository.asyncHomeLayoutTypesetList()
            appSettings.collect {
                _homeLayoutTypesetList.value = it.homeLayoutTypesetList
            }
        }
    }

    private fun initDownloadList() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadManager.initDownloadList()
        }
    }

    fun showBILIUserInfo() {
        viewModelScope.launch {
            initCurrentUserInfo()
        }
    }


    fun onNavigatedFromLogin() {
        viewModelScope.launch {
            if (loginUserInfoState.value.status != ApiStatus.SUCCESS) {
                showBILIUserInfo()
                _uiState.emit(uiState.value.copy(fromLoginEventConsumed = true))
            }
        }
    }


    fun updateWebSpi() {
        // 更新校验
        viewModelScope.launch(Dispatchers.IO) {
            riskManagementRepository.updateWebSpiCookie()
        }
    }

    /**
     * 暂停下载任务
     * [segmentId] 下载任务的ID
     */
    fun pauseDownloadTask(segmentId: Long) {
       viewModelScope.launch { downloadManager.pauseTask(segmentId) }
    }

    @SuppressLint("MissingPermission")
    fun resumeDownloadTask(segmentId: Long) {
        viewModelScope.launch { downloadManager.resumeTask(segmentId) }
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
                                    level = it.data?.level ?: 0,
                                    vipState = it.data?.vipState ?: 0,
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