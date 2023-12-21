package com.imcys.network.repository.auth

import com.imcys.model.login.AuthQrCode
import com.imcys.model.login.LoginResponse

interface IAuthDataSources {
    suspend fun 获取二维码(): AuthQrCode
    suspend fun 轮询登录接口(key: String): LoginResponse
    suspend fun 退出登录()
    suspend fun 检查Cookie是否需要刷新()
    suspend fun 获取refresh_csrf()
    suspend fun 刷新Cookie()
    suspend fun 确认更新Cookie()
}
