package com.imcys.bilibilias.ui.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.utils.AppUtils
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
import com.imcys.bilibilias.network.config.API.App.SSE_HOST
import com.imcys.bilibilias.network.config.API.App.SSE_PATH
import com.imcys.bilibilias.network.config.API.App.SSE_PORT
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.app.AppOldHomeBannerDataBean
import com.imcys.bilibilias.network.model.app.AppUpdateConfigInfo
import com.imcys.bilibilias.network.model.app.BannerConfigInfo
import com.imcys.bilibilias.network.model.app.BulletinConfigInfo
import com.imcys.bilibilias.network.service.AppAPIService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.ClientSSESessionWithDeserialization
import io.ktor.client.plugins.sse.deserialize
import io.ktor.client.plugins.sse.sse
import io.ktor.sse.TypedServerSentEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

class HomeViewModel(
    private val httpClient: HttpClient,
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val usersDataSource: UsersDataSource,
    private val riskManagementRepository: RiskManagementRepository,
    private val downloadManager: DownloadManager,
    private val appSettingsRepository: AppSettingsRepository,
    private val appAPIService: AppAPIService

) : ViewModel() {

    data class UIState(
        val fromLoginEventConsumed: Boolean = false
    )

    val appSettings = appSettingsRepository.appSettingsFlow

    val appSettingsState = appSettingsRepository.appSettingsFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AppSettings.getDefaultInstance()
    )

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

    private val _bannerList = MutableStateFlow<List<BannerConfigInfo>>(emptyList())
    val bannerList = _bannerList.asStateFlow()


    private val _bulletinInfo = MutableStateFlow<BulletinConfigInfo?>(null)
    val bulletinInfo = _bulletinInfo.asStateFlow()

    private val _appUpdateInfo = MutableStateFlow<AppUpdateConfigInfo?>(null)
    val appUpdateInfo = _appUpdateInfo.asStateFlow()

    init {
        initLayoutTypeset()
        initDownloadList()
        requestSSEEvent()
        monitorUserState()
    }

    /**
     * 监听用户登录状态变化
     */
    private fun monitorUserState() {
        viewModelScope.launch(Dispatchers.IO) {
            usersDataSource.users.collect {
                if (it.currentUserId == 0L) {
                    _loginUserInfoState.value = emptyNetWorkResult()
                    _userLoginPlatformList.value = emptyList()
                } else {
                    showBILIUserInfo()
                }
            }
        }
    }

    fun initOldAppInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // 旧版捐助信息
            val bannerInfo = async {
                appAPIService.getAppOldBanner().onSuccess {
                    _bannerList.value = it.textList.mapIndexed { index, string ->
                        BannerConfigInfo(
                            title = string,
                            url = it.imgUrlList.getOrNull(index) ?: "",
                            ref = it.dataList.getOrNull(index) ?: "",
                            id = index,
                            sort = index
                        )
                    }
                }
            }

            val updateInfo = async {
                appAPIService.getAppOldUpdateInfo(AppUtils.getVersion(context = context).second)
                    .onSuccess {
                        _appUpdateInfo.value = AppUpdateConfigInfo(
                            id = 30,
                            version = it.version,
                            url = it.url,
                            feat = it.gxnotice,
                            fix = it.gxnotice,
                            remark = "",
                            forcedUpdate = true,
                            publishDateTime = "最近"
                        )

                        _bulletinInfo.value = BulletinConfigInfo(
                            id = 20,
                            content = it.notice,
                            publishDateTime = "最近"
                        )
                    }
            }
            bannerInfo.start()
            updateInfo.start()


        }
    }

    private fun requestSSEEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                httpClient.sse(
                    host = SSE_HOST,
                    port = SSE_PORT,
                    path = SSE_PATH,
                    deserialize = { typeInfo, jsonString ->
                        val serializer = Json.serializersModule.serializer(typeInfo.kotlinType!!)
                        Json.decodeFromString(serializer, jsonString)
                    }) {

                    while (true) {
                        incoming.collect { event ->
                            handleSSEEvent(event)
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理服务器相关事件
     */
    private fun ClientSSESessionWithDeserialization.handleSSEEvent(event: TypedServerSentEvent<String>) {
        when (event.event) {
            "banner" -> {
                val bannerConfigInfo = deserialize<List<BannerConfigInfo>>(event.data)
                _bannerList.value = bannerConfigInfo ?: emptyList()
            }

            "bulletin" -> {
                val bulletinConfigInfo = deserialize<BulletinConfigInfo>(event.data)
                _bulletinInfo.value = bulletinConfigInfo
            }

            "appUpdate" -> {
                val appUpdateInfo = deserialize<AppUpdateConfigInfo>(event.data)
                _appUpdateInfo.value = appUpdateInfo
            }

            else -> {
                println("Received other event: ${event.event} with data: ${event.data}")
            }
        }
    }

    fun updateLastBulletinContent() {
        viewModelScope.launch {
            appSettingsRepository.updateLastBulletinContent(_bulletinInfo.value?.content ?: "")
        }
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