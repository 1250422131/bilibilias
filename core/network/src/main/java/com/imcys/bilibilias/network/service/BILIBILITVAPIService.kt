package com.imcys.bilibilias.network.service

import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.config.ACCESS_KEY
import com.imcys.bilibilias.network.config.AID
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_LOGIN_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_VIDEO_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_VIDEO_PLAYER_NO_WEBI_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.WEB_VIDEO_PLAYER_URL
import com.imcys.bilibilias.network.config.APP_KEY
import com.imcys.bilibilias.network.config.AUTH_CODE
import com.imcys.bilibilias.network.config.BROWSER_FINGERPRINT
import com.imcys.bilibilias.network.config.BVID
import com.imcys.bilibilias.network.config.CID
import com.imcys.bilibilias.network.config.FNVAL
import com.imcys.bilibilias.network.config.FOURK
import com.imcys.bilibilias.network.config.LOCAL_ID
import com.imcys.bilibilias.network.config.QN
import com.imcys.bilibilias.network.config.SIGN
import com.imcys.bilibilias.network.config.TS
import com.imcys.bilibilias.network.httpRequest
import com.imcys.bilibilias.network.model.TVBILILoginUserInfo
import com.imcys.bilibilias.network.model.TVQRCodeInfo
import com.imcys.bilibilias.network.model.TvQRCodePollInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.utils.BiliAppSigner
import com.imcys.bilibilias.network.utils.WebiTokenUtils.encWbi
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlin.collections.component1
import kotlin.collections.component2

class BILIBILITVAPIService(
    val httpClient: HttpClient
) {
    /**
     * 请求二维码
     */
    suspend fun qrcodeGenerate(): FlowNetWorkResult<TVQRCodeInfo> = httpClient.httpRequest {
        post(TV_QRCODE_GENERATE_URL) {
            setAppParams(
                mutableMapOf(
                    LOCAL_ID to "0",
                )
            )
        }
    }

    suspend fun qrcodePoll(qrcodeKey: String): FlowNetWorkResult<TvQRCodePollInfo> =
        httpClient.httpRequest {
            post(TV_QRCODE_POLL_URL) {
                setAppParams(
                    mutableMapOf(
                        LOCAL_ID to "0",
                        AUTH_CODE to qrcodeKey
                    )
                )
            }
        }


    suspend fun getLoginUserInfo(accessKey: String): FlowNetWorkResult<TVBILILoginUserInfo> =
        httpClient.httpRequest {
            get(TV_LOGIN_INFO_URL) {
                setAppParams(
                    mutableMapOf(
                        ACCESS_KEY to accessKey
                    )
                )
            }
        }

    suspend fun getVideoPlayerInfo(
        cid: Long,
        aid: Long? = null,
        fnval: Int = 4048,
        qn: Int = 127,
        accessKey: String,
    ): FlowNetWorkResult<BILIVideoPlayerInfo> = httpClient.httpRequest {
        val newMap = mutableMapOf<String, String>().apply {
            aid?.let { put("object_id", it.toString()) }
            put(CID, cid.toString())
            put(QN, qn.toString())
            put(FNVAL, fnval.toString())
            put(FOURK, "1")
            put("build", 106500.toString())
            put("mobi_app", "android_tv_yst")
            put("platform", "android")
            put("playurl_type", 1.toString())
            put("device", "android")
            put("mid", "351201307")
        }
        get(TV_VIDEO_PLAYER_URL) {
            header("Cache-Control","no-cache")
            setAppParams(
                mutableMapOf(
                    ACCESS_KEY to accessKey
                ) + newMap
            )
        }
    }

    fun HttpRequestBuilder.setAppParams(params: Map<String, String>) {
        val paramsMap = mutableMapOf(
            APP_KEY to BiliAppSigner.APP_KEY,
            TS to (System.currentTimeMillis() / 1000).toString(),
            LOCAL_ID to "0",
        ) + params
        paramsMap.forEach { (key, value) ->
            parameter(key, value)
        }
        parameter(SIGN, BiliAppSigner.appSign(paramsMap.toMutableMap()))
    }

}