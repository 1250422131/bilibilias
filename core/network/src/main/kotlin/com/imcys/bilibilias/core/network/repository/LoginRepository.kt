package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.login.NavigationBar
import com.imcys.bilibilias.core.model.login.QrcodeGenerate
import com.imcys.bilibilias.core.model.login.QrcodePoll

interface LoginRepository {
    suspend fun 获取二维码(): QrcodeGenerate

    suspend fun 轮询登录(key: String): QrcodePoll

    suspend fun nav(): NavigationBar

    suspend fun exitLogin()

    suspend fun getBilibiliHome()

    suspend fun activeBuvid()
}
