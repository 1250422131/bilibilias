package com.imcys.bilibilias.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.model.BILILoginUserModel
import com.imcys.bilibilias.data.repository.QRCodeLoginRepository
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.database.entity.ASSharedCookieEncoding
import com.imcys.bilibilias.database.entity.BILIUserCookiesEntity
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.AsCookiesStorage
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import io.ktor.http.Cookie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder

class CookieLoginViewModel(
    private val qrCodeLoginRepository: QRCodeLoginRepository,
    private val userInfoRepository: UserInfoRepository,
    private val usersDataSource: UsersDataSource,
    private val asCookiesStorage: AsCookiesStorage
) : ViewModel() {


    private val currentCookies = mutableListOf<Cookie>()

    private val _loginUserInfoState =
        MutableStateFlow<NetWorkResult<BILILoginUserModel?>>(emptyNetWorkResult())
    val loginUserInfoState = _loginUserInfoState.asStateFlow()


    fun checkCookies(cookiesStr: String) {
        currentCookies.addAll(
            cookiesStr
                .split(";")
                .mapNotNull { cookie ->
                    val parts = cookie.trim().split("=", limit = 2)
                    if (parts.size != 2) return@mapNotNull null
                    val (name, value) = parts
                    Cookie(
                        name = name,
                        value = value.urlDecode(),
                        httpOnly = true,
                        secure = true,
                        domain = "bilibili.com",
                        path = "/"
                    )
                }
        )
        viewModelScope.launch {
            asCookiesStorage.updateAllCookies(currentCookies)
            qrCodeLoginRepository.getLoginUserInfo(LoginPlatform.WEB).collect {
                _loginUserInfoState.value = it
            }
        }
    }

    suspend fun saveLoginCookie() {
        val biliLoginUserModel = _loginUserInfoState.value.data
        if (biliLoginUserModel == null) return
        if (biliLoginUserModel.mid == 0L) return
        val oldUserInfo = qrCodeLoginRepository.getBILIUserByMidAndPlatform(
            biliLoginUserModel.mid!!,
            LoginPlatform.WEB,
        )
        val newLoginUserInfo = BILIUsersEntity(
            name = biliLoginUserModel.name ?: "",
            mid = biliLoginUserModel.mid ?: 0L,
            face = biliLoginUserModel.face ?: "",
            level = biliLoginUserModel.level ?: 0,
            vipState = biliLoginUserModel.vipState ?: 0,
            loginPlatform = LoginPlatform.WEB,
            accessToken = null,
            refreshToken = null
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

        usersDataSource.setUserId(userId)
        asCookiesStorage.syncDataBaseCookies()



    }

    private fun String.urlDecode(): String =
        URLDecoder.decode(this, "UTF-8").replace('+', ' ')


}