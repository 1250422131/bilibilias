package com.imcys.bilibilias.network.service

import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_LOGIN_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPI_URL
import com.imcys.bilibilias.network.httpRequest
import com.imcys.bilibilias.network.model.BILILoginUserInfo
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.network.model.QRCodePollInfo
import com.imcys.bilibilias.network.model.WebSpiInfo
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Web平台API
 */
class BILIBILIWebAPIService(
    val httpClient: HttpClient
) {

    /**
     * 请求二维码
     */
    suspend fun qrcodeGenerate(): FlowNetWorkResult<QRCodeInfo> = httpClient.httpRequest {
        get(WEB_QRCODE_GENERATE_URL)
    }

    /**
     * 检查扫码状态
     */
    suspend fun qrcodePoll(qrcodeKey: String): FlowNetWorkResult<QRCodePollInfo> =
        httpClient.httpRequest {
            get(WEB_QRCODE_POLL_URL) {
                parameter("qrcode_key", qrcodeKey)
            }
        }

    /**
     * 获取登录信息
     */
    suspend fun getLoginUserInfo(): FlowNetWorkResult<BILILoginUserInfo> =
        httpClient.httpRequest {
            get(WEB_LOGIN_INFO_URL)
        }


    /**
     * 获取签名:仅Web有
     */
    suspend fun getWebSpiInfo(): FlowNetWorkResult<WebSpiInfo> =
        httpClient.httpRequest {
            get(WEB_SPI_URL)
        }
}