package com.imcys.network.repository.auth

import com.imcys.model.login.*

interface IAuthDataSources {
    suspend fun 获取二维码(): AuthQrCode
    suspend fun 轮询登录接口(key: String): LoginResponse
    suspend fun 退出登录()
    suspend fun 检查Cookie是否需要刷新(): CookieInfo
    suspend fun 获取RefreshCsrf(timestamp: Long): String
    suspend fun 刷新Cookie(csrf: String?, refreshCsrf: String, refreshToken: String): CookieRefresh
    suspend fun 确认更新Cookie(csrf: String?, refreshToken: String)
    suspend fun cookieRefreshChain()
}
