package com.imcys.bilibilias.network.service

import com.imcys.bilibilias.network.config.API.App.OLD_APP_FUNCTION_URL
import com.imcys.bilibilias.network.model.app.AppOldDonateBean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class AppAPIService(
    val httpClient: HttpClient
) {

    /**
     * 请求捐款二维码
     */
    suspend fun getAppOldDonate(): Result<AppOldDonateBean> = runCatching {
        httpClient.get(OLD_APP_FUNCTION_URL) {
            parameter("type", "Donate")
        }.body()
    }

}