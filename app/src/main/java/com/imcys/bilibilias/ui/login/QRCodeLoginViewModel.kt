package com.imcys.bilibilias.ui.login

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.ASSharedCookieEncoding
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.AsCookiesStorage
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.network.model.QRCodePollInfo
import com.imcys.bilibilias.network.model.TvQRCodePollInfo
import io.ktor.http.Cookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QRCodeLoginViewModel(
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val contentResolver: ContentResolver,
    private val usersDataSource: UsersDataSource,
    private val asCookiesStorage: AsCookiesStorage
) : ViewModel() {

    data class UIState(
        var selectedLoginPlatform: LoginPlatform = LoginPlatform.WEB,
        var isLoginLoading: Boolean = false
    )

    private val currentCookies = mutableListOf<Cookie>()
    private var currentQrCodePollInfo: QRCodePollInfo? = null


    var uiState by mutableStateOf(UIState())
        private set

    private val _qrCodeInfoState =
        MutableStateFlow<NetWorkResult<QRCodeInfo?>>(emptyNetWorkResult())
    val qrCodeInfoState = _qrCodeInfoState.asStateFlow()

    private val _qrCodeScanInfoState =
        MutableStateFlow<NetWorkResult<QRCodePollInfo?>>(emptyNetWorkResult())
    val qrCodeScanInfoState = _qrCodeScanInfoState.asStateFlow()

    private val _loginUserInfoState =
        MutableStateFlow<NetWorkResult<BILILoginUserModel?>>(emptyNetWorkResult())

    val loginUserInfoState = _loginUserInfoState.asStateFlow()

    init {
        getLoadLoginQRCodeInfo()
    }

    /**
     * 加载登录二维码
     */
    fun getLoadLoginQRCodeInfo() {
        viewModelScope.launch {
            qrCodeLoginRepository.getLoginQRCodeInfo(uiState.selectedLoginPlatform).collect {
                _qrCodeInfoState.emit(it)
                when (it) {
                    is NetWorkResult.Success<*> -> {
                        it.data?.let { data ->
                            checkScanQRState(data.qrcodeKey)
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    /**
     * 更新登录平台
     */
    fun updateLoginPlatform(loginPlatform: LoginPlatform) {
        uiState = uiState.copy(selectedLoginPlatform = loginPlatform)
        getLoadLoginQRCodeInfo()
    }


    /**
     * 获取登录信息
     */
    fun getLoginUserInfo(
        qrCodePollInfo: QRCodePollInfo? = null,
    ) {
        uiState = uiState.copy(isLoginLoading = true)
        viewModelScope.launch {
            currentQrCodePollInfo = qrCodePollInfo
            qrCodeLoginRepository.getLoginUserInfo(
                uiState.selectedLoginPlatform,
                qrCodePollInfo?.accessToken
            )
                .collect {
                    _loginUserInfoState.emit(it)
                }
        }
    }

    /**
     * 保存登录用户信息
     */
    fun saveLoginInfo(biliLoginUserModel: BILILoginUserModel?, onSaveFinish: () -> Unit) {
        if (biliLoginUserModel == null) return

        viewModelScope.launch(Dispatchers.IO) {
            if (biliLoginUserModel.mid == 0L) return@launch
            val oldUserInfo = qrCodeLoginRepository.getBILIUserByMidAndPlatform(
                biliLoginUserModel.mid!!,
                uiState.selectedLoginPlatform,
            )
            val newLoginUserInfo = BILIUsersEntity(
                name = biliLoginUserModel.name ?: "",
                mid = biliLoginUserModel.mid ?: 0L,
                face = biliLoginUserModel.face ?: "",
                level = biliLoginUserModel.level ?: 0,
                loginPlatform = uiState.selectedLoginPlatform,
                accessToken = currentQrCodePollInfo?.accessToken,
                refreshToken = currentQrCodePollInfo?.refreshToken
            )

            val userId = if (oldUserInfo == null) {
                qrCodeLoginRepository.saveLoginInfo(newLoginUserInfo)
            } else {
                newLoginUserInfo.apply { newLoginUserInfo.id = oldUserInfo.id }
                qrCodeLoginRepository.updateBILIUser(newLoginUserInfo)
                newLoginUserInfo.id
            }

            // 新增Cookie存储
            qrCodeLoginRepository.deleteBILICookiesByUid(userId)

            // 新增
            currentCookies.forEach {
                val cookie = BILIUserCookiesEntity(
                    userId = userId,
                    name = it.name,
                    value = it.value,
                    path = it.path,
                    secure = it.secure,
                    domain = it.domain,
                    encoding = ASSharedCookieEncoding.valueOf(it.encoding.name),
                    httpOnly = it.httpOnly
                )
                qrCodeLoginRepository.insertBILIUserCookie(cookie)
            }

            if (uiState.selectedLoginPlatform == LoginPlatform.WEB) {
                usersDataSource.setUserId(userId)
                asCookiesStorage.syncDataBaseCookies()
            }

            uiState = uiState.copy(isLoginLoading = false)
            launch(Dispatchers.Main) {
                onSaveFinish.invoke()
            }

        }
    }


    /**
     * 保存二维码
     */
    fun saveQRCodeImageToGallery(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            val imageUrl = qrCodeInfoState.value.data
            if (imageUrl?.url == null) return@launch
            runCatching {
                // 使用 Coil 下载图片
                withContext(Dispatchers.IO) {
                    val request = ImageRequest.Builder(context)
                        .data("https://pan.misakamoe.com/qrcode/?url=$imageUrl")
                        .build()
                    context.imageLoader.execute(request).image?.toBitmap()!!
                }
            }.onSuccess {
                saveImageWithMediaStore(it, context)
            }.onFailure {
                Toast.makeText(context, "下载失败，请重新尝试。", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveImageWithMediaStore(bitmap: Bitmap, context: Context) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "BILIBILIAS_LOGIN_QR.jpeg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_PICTURES}/BILIBILIAS"
            )
        }

        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        if (uri != null) {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show()
        }

    }


    fun goToScanQR(context: Context) {
        runCatching {
            Intent(Intent.ACTION_VIEW, "bilibili://qrscan".toUri()).also {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        }.onFailure {
            Toast.makeText(
                context,
                "你还没有安装哔哩哔哩哦~",
                Toast.LENGTH_SHORT,
            ).show()
        }

    }


    private var checkScanQRJob: Job? = null

    /**
     * 检测登录状态
     */
    private fun checkScanQRState(qrcodeKey: String) {

        var finish = false
        if (checkScanQRJob != null) {
            checkScanQRJob?.cancel()
            checkScanQRJob = null
        }

        checkScanQRJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive && !finish) {
                qrCodeLoginRepository.getQRScanState(uiState.selectedLoginPlatform, qrcodeKey)
                    .collect {
                        _qrCodeScanInfoState.emit(it)
                        when (it) {
                            is NetWorkResult.Success<*> -> {
                                handleHeaderCookie(it.responseData?.responseHeader)
                                handleResponseCookie(it.data?.cookieInfo)
                                if (it.data?.code == 0) {
                                    finish = true
                                }
                            }

                            else -> {}
                        }
                    }
                delay(3000L)
            }
        }
    }

    /**
     * 保存Cookie
     */
    private fun handleResponseCookie(cookieInfo: TvQRCodePollInfo.CookieInfo?) {
        if (cookieInfo == null) return
        viewModelScope.launch {
            currentCookies.clear()
            cookieInfo.cookies.forEach { cookie ->
                val cookieInfo = Cookie(
                    name = cookie.name,
                    value = cookie.value,
                    httpOnly = cookie.httpOnly == 1L,
                    secure = cookie.secure == 1L,
                    domain = "bilibili.com",
                    path = "/"
                )
                currentCookies.add(cookieInfo)
            }
        }

    }

    /**
     * 保存Cookie
     */
    private fun handleHeaderCookie(responseHeader: Set<Map.Entry<String, List<String>>>?) {
        responseHeader?.forEach {
            if (it.key == "set-cookie") {
                currentCookies.clear()
                currentCookies.addAll(asCookiesStorage.getAllCookies())
            }
        }
    }


}
