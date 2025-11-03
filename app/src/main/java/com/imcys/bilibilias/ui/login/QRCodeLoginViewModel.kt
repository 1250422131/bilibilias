package com.imcys.bilibilias.ui.login


import com.imcys.bilibilias.R
import androidx.compose.ui.res.stringResource
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
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
import androidx.core.graphics.createBitmap
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

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
                vipState = biliLoginUserModel.vipState ?: 0,
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
            val imageUrl = qrCodeInfoState.value.data?.url
            if (imageUrl == null) {
                Toast.makeText(context, stringResource(R.string.login_xia_zai_shi_bai__qing_shu), Toast.LENGTH_SHORT).show()
                return@launch
            }
            runCatching {
                // 使用 Coil 下载图片
                withContext(Dispatchers.IO) {
                    val request = ImageRequest.Builder(context)
                        .data(
                            "https://pan.misakamoe.com/qrcode/?url=${
                                URLEncoder.encode(
                                    imageUrl,
                                    "UTF-8"
                                )
                            }"
                        )
                        .build()
                    context.imageLoader.execute(request).image?.toBitmap()!!
                }
            }.onSuccess {
                saveImageWithMediaStore(it, context)
            }.onFailure {
                Toast.makeText(context, stringResource(R.string.login_xia_zai_shi_bai__qing_cho), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveImageWithMediaStore(bitmap: Bitmap, context: Context) {
        val borderWidth = 20 // 白色边框宽度
        val bitmapWithBorder = addWhiteBorder(bitmap, borderWidth)

        // 时间戳
        val fileName = "QR_${System.currentTimeMillis()}.jpeg"
        val relativePath = "${Environment.DIRECTORY_PICTURES}/BILIBILIAS"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            }
            val uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri == null) return
            contentResolver.openOutputStream(uri, "rwt")?.use { outputStream ->
                bitmapWithBorder.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } else {
            val picturesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val dir = File(picturesDir, "BILIBILIAS")
            if (!dir.exists()) dir.mkdirs()
            val file = File(dir, fileName)
            try {
                FileOutputStream(file).use { fos ->
                    bitmapWithBorder.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                }
                // 通知媒体库刷新
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, file.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                null // 已经保存到文件系统
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        Toast.makeText(context, stringResource(R.string.login_bao_cun_cheng_gong), Toast.LENGTH_SHORT).show()
    }

    fun addWhiteBorder(originalBitmap: Bitmap, borderWidth: Int): Bitmap {
        // 兼容所有 Android 版本，避免硬件位图问题
        val safeBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (originalBitmap.config == Bitmap.Config.HARDWARE) {
                originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                originalBitmap
            }
        } else {
            // 低版本直接复制为可变位图
            if (!originalBitmap.isMutable) {
                originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
            } else {
                originalBitmap
            }
        }
        val newWidth = safeBitmap.width + borderWidth * 2
        val newHeight = safeBitmap.height + borderWidth * 2
        val borderedBitmap = createBitmap(newWidth, newHeight)
        val canvas = Canvas(borderedBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(safeBitmap, borderWidth.toFloat(), borderWidth.toFloat(), null)
        return borderedBitmap
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
                stringResource(R.string.login_ni_hai_mei_you_an_zhuang),
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
    private suspend fun handleHeaderCookie(responseHeader: Set<Map.Entry<String, List<String>>>?) {
        responseHeader?.forEach {
            if (it.key == "Set-Cookie") {
                currentCookies.clear()
                currentCookies.addAll(asCookiesStorage.getAllCookies())
            }
        }
    }


}
