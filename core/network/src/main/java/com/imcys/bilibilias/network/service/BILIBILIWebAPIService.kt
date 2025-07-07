package com.imcys.bilibilias.network.service

import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.config.ACCESS_ID
import com.imcys.bilibilias.network.config.API.BILIBILI.SPACE_BASE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_LOGIN_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_RELATION_STAT_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPACE_ARC_SEARCH
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPACE_UPSTAT_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPI_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_WEBI_ACC_INFO_URL
import com.imcys.bilibilias.network.config.BROWSER_FINGERPRINT
import com.imcys.bilibilias.network.config.MID
import com.imcys.bilibilias.network.config.W_WEBID
import com.imcys.bilibilias.network.httpRequest
import com.imcys.bilibilias.network.model.BILILoginUserInfo
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.network.model.QRCodePollInfo
import com.imcys.bilibilias.network.model.WebSpiInfo
import com.imcys.bilibilias.network.model.user.BILISpaceArchiveInfo
import com.imcys.bilibilias.network.model.user.BILIUserAccInfo
import com.imcys.bilibilias.network.model.user.BILIUserRelationStatInfo
import com.imcys.bilibilias.network.model.user.BILIUserSpaceUpStat
import com.imcys.bilibilias.network.utils.WebiTokenUtils.encWbi
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.decodeURLPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

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


    /**
     * 获取签名:仅Web有，TV共用
     */
    suspend fun getUserAccInfo(mid: Long): FlowNetWorkResult<BILIUserAccInfo> =
        httpClient.httpRequest {
            val newMap = mapOf(
                MID to mid.toString(),
            ) + BROWSER_FINGERPRINT + accessUserSpaceGetRenderData(mid)
            get(WEB_WEBI_ACC_INFO_URL) {
                encWbi(newMap).forEach { (k, v) ->
                    parameter(k, v)
                }
            }
        }


    suspend fun getSpaceUpStat(mid: Long): FlowNetWorkResult<BILIUserSpaceUpStat> =
        httpClient.httpRequest {
            get(WEB_SPACE_UPSTAT_URL) {
                parameter(MID, mid)
            }
        }

    suspend fun getRelationStat(mid: Long): FlowNetWorkResult<BILIUserRelationStatInfo> =
        httpClient.httpRequest {
            get(WEB_RELATION_STAT_URL) {
                parameter("vmid", mid)
            }
        }


    /**
     * 获取投稿视频：Web和TV共用
     */
    suspend fun getSpaceArchiveInfo(
        mid: Long,
        pn: Int = 1,
        ps: Int = 2
    ): FlowNetWorkResult<BILISpaceArchiveInfo> =
        httpClient.httpRequest {
            val newMap = mapOf(
                MID to mid.toString(),
                "pn" to pn.toString(),
                "ps" to ps.toString(),
                "platform" to "web",
                "index" to "1",
                "order" to "pubdate"
            ) + BROWSER_FINGERPRINT + accessUserSpaceGetRenderData(mid)
            get(WEB_SPACE_ARC_SEARCH) {
                encWbi(newMap).forEach { (k, v) ->
                    parameter(k, v)
                }
            }
        }


    private suspend fun accessUserSpaceGetRenderData(mid: Long): Map<String, String> = withContext(
        Dispatchers.IO
    ) {
        val result = runCatching {
            val response = httpClient.get(SPACE_BASE_URL + mid).bodyAsText()
            val regex = "\"__RENDER_DATA__\" type=\"application/json\">(.*)</script>".toRegex()
            val result =
                regex.find(response)?.groupValues?.get(1)?.ifEmpty { return@runCatching emptyMap() }
                    ?: return@runCatching emptyMap()
            val accessId =
                Json.parseToJsonElement(result.decodeURLPart()).jsonObject[ACCESS_ID]?.jsonPrimitive?.content
                    ?: ""
            mapOf(W_WEBID to accessId)
        }
        result.getOrNull() ?: emptyMap()
    }
}