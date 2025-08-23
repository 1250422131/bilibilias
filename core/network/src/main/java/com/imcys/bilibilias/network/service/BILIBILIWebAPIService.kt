package com.imcys.bilibilias.network.service

import android.se.omapi.Session
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.config.ACCESS_ID
import com.imcys.bilibilias.network.config.AID
import com.imcys.bilibilias.network.config.API.BILIBILI.SPACE_BASE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_LOGIN_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_PGC_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_RELATION_STAT_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPACE_ARC_SEARCH
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPACE_UPSTAT_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_SPI_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_VIDEO_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_WEBI_ACC_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_WEBI_PGC_SEASON_VIEW
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_WEBI_VIDEO_VIEW
import com.imcys.bilibilias.network.config.BROWSER_FINGERPRINT
import com.imcys.bilibilias.network.config.BROWSER_USER_AGENT
import com.imcys.bilibilias.network.config.BVID
import com.imcys.bilibilias.network.config.CID
import com.imcys.bilibilias.network.config.EP_ID
import com.imcys.bilibilias.network.config.FNVAL
import com.imcys.bilibilias.network.config.FOURK
import com.imcys.bilibilias.network.config.MID
import com.imcys.bilibilias.network.config.QN
import com.imcys.bilibilias.network.config.REFERER
import com.imcys.bilibilias.network.config.SEASON_ID
import com.imcys.bilibilias.network.config.TRY_LOOK
import com.imcys.bilibilias.network.config.W_WEBID
import com.imcys.bilibilias.network.httpRequest
import com.imcys.bilibilias.network.model.BILILoginUserInfo
import com.imcys.bilibilias.network.model.BiliApiResponse
import com.imcys.bilibilias.network.model.QRCodeInfo
import com.imcys.bilibilias.network.model.QRCodePollInfo
import com.imcys.bilibilias.network.model.WebSpiInfo
import com.imcys.bilibilias.network.model.user.BILISpaceArchiveInfo
import com.imcys.bilibilias.network.model.user.BILIUserSpaceAccInfo
import com.imcys.bilibilias.network.model.user.BILIUserRelationStatInfo
import com.imcys.bilibilias.network.model.user.BILIUserSpaceUpStat
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoViewInfo
import com.imcys.bilibilias.network.utils.WebiTokenUtils.encWbi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
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
    val httpClient: HttpClient,
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
     * 获取WebI签名信息
     */
    suspend fun getWebIInfoNoCheckLogin(): BiliApiResponse<BILILoginUserInfo> =
        httpClient.get(WEB_LOGIN_INFO_URL).body()


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
    suspend fun getUserAccInfo(mid: Long): FlowNetWorkResult<BILIUserSpaceAccInfo> =
        httpClient.httpRequest {
            val newMap = mapOf(
                MID to mid.toString(),
            ) + BROWSER_FINGERPRINT + accessUserSpaceGetRenderData(mid)
            get(WEB_WEBI_ACC_INFO_URL) {
                header(REFERER, "${SPACE_BASE_URL}${mid}")
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

    /**
     * 获取视频详情：通用接口
     */
    suspend fun getVideoView(
        bvId: String?,
        aid: String?
    ): FlowNetWorkResult<BILIVideoViewInfo> = httpClient.httpRequest {
        val newMap = mutableMapOf<String, String>().apply {
            bvId?.let { put(BVID, it) }
            aid?.let { put(AID, it) }
        } + BROWSER_FINGERPRINT
        get(WEB_WEBI_VIDEO_VIEW) {
            encWbi(newMap).forEach { (k, v) ->
                parameter(k, v)
            }
        }
    }


    suspend fun getDonghuaSeasonViewInfo(
        epId: Long?,
        seasonId: Long?
    ): FlowNetWorkResult<BILIDonghuaSeasonInfo> = httpClient.httpRequest {
        val newMap = mutableMapOf<String, String>().apply {
            epId?.let { put(EP_ID, it.toString()) }
            seasonId?.let { put(SEASON_ID, it.toString()) }
        }
        get(WEB_WEBI_PGC_SEASON_VIEW) {
            newMap.forEach { (k, v) ->
                parameter(k, v)
            }
        }
    }

    suspend fun getDonghuaPlayerInfo(
        epId: Long?,
        seasonId: Long?,
        fnval: Int = 12240,
        qn: Int = 116,
    ): FlowNetWorkResult<BILIDonghuaPlayerInfo> = httpClient.httpRequest {

        val newMap = mutableMapOf<String, String>().apply {
            epId?.let { put(EP_ID, it.toString()) }
            seasonId?.let { put(SEASON_ID, it.toString()) }
            put(QN, qn.toString())
            put(FNVAL, fnval.toString())
            put(FOURK, "1")
        }

        get(WEB_PGC_PLAYER_URL) {
            newMap.forEach { (k, v) ->
                parameter(k, v)
            }
        }
    }

    suspend fun getVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
        fnval: Int = 4048,
        qn: Int = 127,
        tryLook: String? = null
    ): FlowNetWorkResult<BILIVideoPlayerInfo> = httpClient.httpRequest {
        val newMap = mutableMapOf<String, String>().apply {
            bvId?.let { put(BVID, it) }
            aid?.let { put(AID, it.toString()) }
            put(CID, cid.toString())
            put(QN, qn.toString())
            put(FNVAL, fnval.toString())
            put(FOURK, "1")
        } + BROWSER_FINGERPRINT

        get(WEB_VIDEO_PLAYER_URL) {
            encWbi(newMap).forEach { (k, v) ->
                parameter(k, v)
            }
        }
    }

    /**
     * 用来解析正确的地址
     */
    suspend fun shortLink(url: String): String = httpClient.get(url)
        .request
        .url
        .toString()

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