package com.imcys.bilibilias.network.service

import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.config.ACCESS_KEY
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_LOGIN_INFO_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_PGC_PLAYER_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_GENERATE_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_QRCODE_POLL_URL
import com.imcys.bilibilias.network.config.API.BILIBILI.TV_VIDEO_PLAYER_URL
import com.imcys.bilibilias.network.config.APP_KEY
import com.imcys.bilibilias.network.config.AUTH_CODE
import com.imcys.bilibilias.network.config.CID
import com.imcys.bilibilias.network.config.EP_ID
import com.imcys.bilibilias.network.config.FNVAL
import com.imcys.bilibilias.network.config.FOURK
import com.imcys.bilibilias.network.config.LOCAL_ID
import com.imcys.bilibilias.network.config.MID
import com.imcys.bilibilias.network.config.QN
import com.imcys.bilibilias.network.config.SIGN
import com.imcys.bilibilias.network.config.TS
import com.imcys.bilibilias.network.httpRequest
import com.imcys.bilibilias.network.model.TVBILILoginUserInfo
import com.imcys.bilibilias.network.model.TVQRCodeInfo
import com.imcys.bilibilias.network.model.TvQRCodePollInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.utils.BiliAppSigner
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.ParametersBuilder
import io.ktor.http.contentType
import io.ktor.http.parameters

class BILIBILITVAPIService(
    val httpClient: HttpClient
) {

    /**
     * 请求二维码
     */
    suspend fun qrcodeGenerate(): FlowNetWorkResult<TVQRCodeInfo> = httpClient.httpRequest {
        submitForm(TV_QRCODE_GENERATE_URL, formParameters = parameters {
            setAppFormData(BiliAppSigner.biliTvDeviceInfo + mutableMapOf("auth_code" to ""))
        }) {
            contentType(ContentType.Application.FormUrlEncoded)
        }
    }

    suspend fun qrcodePoll(qrcodeKey: String): FlowNetWorkResult<TvQRCodePollInfo> =
        httpClient.httpRequest {
            submitForm(TV_QRCODE_POLL_URL, formParameters = parameters {
                setAppFormData(
                    mutableMapOf(
                        AUTH_CODE to qrcodeKey
                    ) + BiliAppSigner.biliTvDeviceInfo
                )
            }) {
                contentType(ContentType.Application.FormUrlEncoded)
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

    suspend fun checkLoginUserInfo(accessKey: String): TVBILILoginUserInfo =
        httpClient.get(TV_LOGIN_INFO_URL) {
            setAppParams(
                mutableMapOf(
                    ACCESS_KEY to accessKey
                )
            )
        }.body()

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
            put("mid", "0")
        }
        get(TV_VIDEO_PLAYER_URL) {
            header("Cache-Control", "no-cache")
            setAppParams(
                mutableMapOf(
                    ACCESS_KEY to accessKey
                ) + newMap
            )
        }
    }


    suspend fun getDonghuaPlayerInfo(
        cid: Long?,
        aid: Long? = null,
        epId: Long,
        fnval: Int = 4048,
        qn: Int = 127,
        accessKey: String,
    ): FlowNetWorkResult<BILIDonghuaPlayerInfo> = httpClient.httpRequest {
        val newMap = mutableMapOf<String, String>().apply {
            aid?.let { put("object_id", it.toString()) }
            cid?.let { put(CID, it.toString()) }
            put(QN, qn.toString())
            put(FNVAL, fnval.toString())
            put(FOURK, "1")
            put(EP_ID, epId.toString())
            put("build", 106500.toString())
            put("mobi_app", "android_tv_yst")
            put("platform", "android")
            put("playurl_type", 1.toString())
            put("device", "android")
            put(MID, "0")
            put("expire", "0")
            put("fnver", "0")

        }
        get(TV_PGC_PLAYER_URL) {
            header("Cache-Control", "no-cache")
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

    fun ParametersBuilder.setAppFormData(params: Map<String, String>) {
        val paramsMap = mutableMapOf(
            APP_KEY to BiliAppSigner.APP_KEY,
            TS to (System.currentTimeMillis() / 1000).toString(),
        ) + params

        paramsMap.forEach { (key, value) ->
            append(key, value)
        }
        append(SIGN, BiliAppSigner.appSign(paramsMap.toMutableMap()) ?: "")

    }

}