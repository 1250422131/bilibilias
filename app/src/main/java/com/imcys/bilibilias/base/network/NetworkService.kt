package com.imcys.bilibilias.base.network

import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.base.utils.TokenUtils.encWbi
import com.imcys.bilibilias.base.utils.TokenUtils.key
import com.imcys.bilibilias.base.utils.TokenUtils.setKey
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.model.common.BangumiFollowList
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.di.AsCookiesStorage
import com.imcys.bilibilias.home.ui.model.BangumiPlayBean
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.CollectionDataBean
import com.imcys.bilibilias.home.ui.model.CollectionResultBean
import com.imcys.bilibilias.home.ui.model.DashBangumiPlayBean
import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean
import com.imcys.bilibilias.home.ui.model.OldDonateBean
import com.imcys.bilibilias.home.ui.model.OldHomeAdBean
import com.imcys.bilibilias.home.ui.model.OldHomeBannerDataBean
import com.imcys.bilibilias.home.ui.model.OldToolItemBean
import com.imcys.bilibilias.home.ui.model.OldUpdateDataBean
import com.imcys.bilibilias.home.ui.model.PlayHistoryBean
import com.imcys.bilibilias.home.ui.model.UpStatBeam
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.imcys.bilibilias.home.ui.model.UserCardBean
import com.imcys.bilibilias.home.ui.model.UserCreateCollectionBean
import com.imcys.bilibilias.home.ui.model.UserNavDataModel
import com.imcys.bilibilias.home.ui.model.UserWorksBean
import com.imcys.bilibilias.home.ui.model.VideoBaseBean
import com.imcys.bilibilias.home.ui.model.VideoCoinAddBean
import com.imcys.bilibilias.home.ui.model.VideoPageListData
import com.imcys.bilibilias.home.ui.model.VideoPlayBean
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.http.decodeURLPart
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(
    private val httpClient: HttpClient,
    private val asCookiesStorage: AsCookiesStorage
) {
    @Deprecated("不需要")
    private val ioDispatcher = Dispatchers.IO
    suspend fun getDashBangumiPlayInfo(cid: Long, qn: Int): DashBangumiPlayBean =
        runCatchingOnWithContextIo {
            httpClient.get("pgc/player/web/playurl") {
                refererBILIHarder()
                parameter("cid", cid)
                parameter("qn", qn)
                parameter("fnval", 4048)
                parameter("fourk", 1)
            }.body()
        }

    private suspend inline fun <reified T> viewPlayUrl(
        bvid: String,
        cid: String,
        qn: Int,
    ): T = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.videoPlayPath) {
            parameterBVID(bvid)
            parameterCID(cid)
            parameter("qn", qn)
            parameter("fnval", 4048)
            parameter("fourk", 1)
        }.body()
    }

    suspend fun pgcPlayUrl(cId: Long, qn: Int): DashBangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.bangumiPlayPath) {
            parameterCID(cId.toString())
            parameter("qn", qn)
            parameter("fnval", "4048")
            parameter("fourk", "1")
        }.body()
    }

    suspend fun viewDash(bvid: String, cid: Long, qn: Int): DashVideoPlayBean =
        runCatchingOnWithContextIo {
            viewPlayUrl(bvid, cid.toString(), qn)
        }

    // ---------------------------------------------------------------------------------------------
    suspend fun getDashVideoPlayInfo(bvid: String, cid: Long, qn: Int): DashVideoPlayBean =
        runCatchingOnWithContextIo { videoPlayPath(bvid, cid.toString(), qn) }

    suspend fun exitUserLogin(crsf: String): String = runCatchingOnWithContextIo {
        httpClient.post(BilibiliApi.exitLogin) {
            parameter("biliCSRF", crsf)
        }.body()
    }

    suspend fun n10(bvid: String, cid: Long): DashVideoPlayBean = runCatchingOnWithContextIo {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    suspend fun getPlayHistory(max: Long, viewAt: Long): PlayHistoryBean =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.userPlayHistoryPath) {
                parameter("max", max)
                parameter("view_at", viewAt)
                parameter("type", "archive")
            }
                .body()
        }

    private suspend inline fun <reified T> videoPlayPath(
        bvid: String,
        cid: String,
        qn: Int,
        fnval: Int = 4048,
    ): T = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.videoPlayPath) {
            refererBILIHarder()
            parameterBVID(bvid)
            parameterCID(cid)
            parameter("qn", qn)
            parameter("fnval", fnval)
            parameter("fourk", 1)
        }.body()
    }

    private fun HttpRequestBuilder.parameterBVID(bvid: String): Unit =
        url.parameters.append("bvid", bvid)

    private fun HttpRequestBuilder.parameterAID(aid: String): Unit =
        url.parameters.append("aid", aid)

    private fun HttpRequestBuilder.parameterCID(cid: String): Unit =
        url.parameters.append("cid", cid)

    private fun HttpRequestBuilder.parameterUpMID(upMid: String): Unit =
        url.parameters.append("up_mid", upMid)

    private fun HttpRequestBuilder.refererBILIHarder(): Unit =
        header(HttpHeaders.Referrer, BILIBILI_URL)

    private fun HttpRequestBuilder.parameterMID(mid: String): Unit =
        url.parameters.append("mid", mid)

    private fun HttpRequestBuilder.parameterEpID(epid: String): Unit =
        url.parameters.append("ep_id", epid)

    // ---------------------------------------------------------------------------------------------
    suspend fun n3(bvid: String, cid: Long, qn: Int): VideoPlayBean = runCatchingOnWithContextIo {
        videoPlayPath(bvid, cid.toString(), qn, fnval = 0)
    }

    suspend fun getVideoPlayInfo(bvid: String, cid: Long): VideoPlayBean =
        runCatchingOnWithContextIo {
            videoPlayPath(bvid, cid.toString(), 64, fnval = 0)
        }

    // ---------------------------------------------------------------------------------------------
    suspend fun n4(cid: Long, qn: Int): BangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get("pgc/player/web/playurl") {
            refererBILIHarder()
            parameter("cid", cid)
            parameter("qn", qn)
            parameter("fnval", 0)
            parameter("fourk", 1)
        }.body()
    }

    suspend fun n16(epid: Long): BangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get("pgc/player/web/playurl") {
            refererBILIHarder()
            parameter("ep_id", epid)
            parameter("qn", 64)
            parameter("fnval", 0)
            parameter("fourk", 1)
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun getVideoBaseBean(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        getVideoBaseInfoByBvid(bvid)
    }

    suspend fun getVideoBaseInfoByBvid(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getVideoDataPath) { parameterBVID(bvid) }.body()
    }

    suspend fun getVideoBaseInfoByAid(aid: String): VideoBaseBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getVideoDataPath) { parameterAID(aid) }.body()
    }

    suspend fun n26(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        getVideoBaseInfoByBvid(bvid)
    }

    // ---------------------------------------------------------------------------------------------

    suspend fun getBILIHome(): String = runCatchingOnWithContextIo {
        httpClient.get(BILIBILI_URL).body()
    }

    suspend fun biliUserLogin(qrcodeKey: String): LoginStateBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getLoginStatePath) {
            parameter("qrcode_key", qrcodeKey)
        }.body()
    }

    suspend fun getLoginQRData(): LoginQrcodeBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getLoginQRPath).body()
    }

    suspend fun getMyUserData(): MyUserData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    suspend fun postAsData(
        aid: Long,
        bvid: String?,
        mid: Long,
        name: String?,
        tName: String?,
        copyright: Int,
        UserName: String? = null,
        UserUID: Long? = null,
    ): String = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.appAddAsVideoData) {
            parameter("Aid", aid)
            parameter("Bvid", bvid)
            parameter("Mid", mid)
            parameter("Upname", name)
            parameter("Tname", tName)
            parameter("Copyright", copyright)
            parameter("UserName", UserName)
            parameter("UserUID", UserUID)
        }.body()
    }

    suspend fun getDanmuBytes(cid: Long) = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.videoDanMuPath) {
            refererBILIHarder()
            parameter("oid", cid)
        }.readRawBytes()
    }

    suspend fun getOldToolItem(): OldToolItemBean = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.appFunction) {
            parameter("type", "oldToolItem")
        }.body()
    }

    suspend fun getUserCollection(id: Long, pn: Int): CollectionDataBean =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.userCollectionDataPath) {
                parameter("media_id", id)
                parameter("pn", pn)
                parameter("ps", "20")
            }
                .body()
        }

    suspend fun getDonateData(): OldDonateBean = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.appFunction) {
            parameter("type", "Donate")
        }.body()
    }

    suspend fun getOldHomeBannerData(): OldHomeBannerDataBean = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.updateDataPath) {
            parameter("type", "banner")
        }.body()
    }

    suspend fun getBangumiFollow(vmid: Long, type: Int, pn: Int, ps: Int): BangumiFollowList =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.bangumiFollowPath) {
                parameter("vmid", vmid)
                parameter("type", type)
                parameter("pn", pn)
                parameter("ps", ps)
            }
                .body()
        }

    suspend fun getUpdateData(): OldUpdateDataBean = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.updateDataPath) {
            parameter("type", "json")
            parameter("version", BiliBiliAsApi.version)
        }
            .body()
    }

    suspend fun getOldHomeAd(): OldHomeAdBean = runCatchingOnWithContextIo {
        httpClient.get(BiliBiliAsApi.appFunction) {
            parameter("type", "oldHomeAd")
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun getBangumiSeasonBeanByEpid(epid: Long): BangumiSeasonBean =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.bangumiVideoDataPath) {
                parameterEpID(epid.toString())
            }.body()
        }

    // ---------------------------------------------------------------------------------------------
    suspend fun getUserNavInfo(): UserNavDataModel = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userNavDataPath).body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n11(mid: Long): UserBaseBean =
        runCatchingOnWithContextIo {
            val newMap = mapOf("mid" to mid.toString()) + accessUserSpaceGetRenderData(mid)
            httpClient.get(BilibiliApi.userBaseDataPath) {
                encWbi(newMap).forEach { (k, v) ->
                    parameter(k, v)
                }
            }.body()
        }

    // ---------------------------------------------------------------------------------------------
    suspend fun getUserCardData(mid: Long): UserCardBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getUserCardPath) {
            encWbi(
                mapOf(
                    "mid" to mid.toString(),
                )
            ).forEach { (k, v) ->
                parameter(k, v)
            }
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n34(): UserCreateCollectionBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userCreatedScFolderPath) {
            parameterUpMID(BaseApplication.asUser.mid.toString())
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun getVideoPageListData(bvid: String): VideoPageListData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.videoPageListPath) {
            parameterBVID(bvid)
        }.body()
    }

    suspend fun getUserWorkData(mid: Long, page: Int): UserWorksBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userWorksPath) {
            refererBILIHarder()
            encWbi(
                mapOf(
                    "mid" to mid.toString(),
                    "pn" to page.toString(),
                    "ps" to "30"
                )
            ).forEach { (k, v) ->
                parameter(k, v)
            }
        }.body()
    }

    // ----------------------------------------------------------------------------------------------
    suspend fun getUpStateInfo(): UpStatBeam = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userUpStat) {
            parameter("mid", BaseApplication.asUser.mid)
        }.body()
    }

    suspend fun getUserInfoData(mid: Long): UserInfoBean =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.getUserInfoPath) {
                encWbi(
                    mapOf("mid" to mid.toString()) + accessUserSpaceGetRenderData(mid)
                )
                    .forEach { (k, v) ->
                        parameter(k, v)
                    }
            }.body()
        }

    private suspend fun accessUserSpaceGetRenderData(mid: Long): Map<String, String> {
        val response = httpClient.get(BilibiliApi.spacePath + mid).bodyAsText()
        val regex = "\"__RENDER_DATA__\" type=\"application/json\">(.*)</script>".toRegex()
        val result = regex.find(response)?.groupValues?.get(1)?.ifEmpty { return emptyMap() }
            ?: return emptyMap()
        val accessId =
            Json.parseToJsonElement(result.decodeURLPart()).jsonObject["access_id"]?.jsonPrimitive?.content
                ?: ""
        return mapOf("w_webid" to accessId)
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun videoLike(bvid: String): LikeVideoBean = runCatchingOnWithContextIo {
        httpClient.submitForm(
            url = BilibiliApi.videLikePath,
            formParameters = parameters {
                append("csrf", asCookiesStorage.getCookieValue("bili_jct") ?: "")
                append("like", "1")
                append("bvid", bvid)
            }
        ).body()
    }

    suspend fun n32(bvid: String): LikeVideoBean = runCatchingOnWithContextIo {
        httpClient.submitForm(
            url = BilibiliApi.videLikePath,
            formParameters = parameters {
                append("csrf", asCookiesStorage.getCookieValue("bili_jct") ?: "")
                append("like", "2")
                append("bvid", bvid)
            },
        ).body()
    }

    suspend fun n33(bvid: String): VideoCoinAddBean = runCatchingOnWithContextIo {
        httpClient.submitForm(
            url = BilibiliApi.videoCoinAddPath,
            formParameters = parameters {
                append("csrf", asCookiesStorage.getCookieValue("bili_jct") ?: "")
                append("bvid", bvid)
                append("multiply", "2")
            },
        ).body()
    }

    suspend fun n35(toString: String, addMediaIds: String): CollectionResultBean =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.videoCollectionSetPath) {
                parameter("rid", toString)
                parameter("add_media_ids", addMediaIds)
                parameter("csrf", asCookiesStorage.getCookieValue("bili_jct") ?: "")
                parameter("type", "2")
            }.body()
        }

    private suspend inline fun <reified T> runCatchingOnWithContextIo(
        noinline block: suspend CoroutineScope.() -> T
    ): T {
        return withContext(ioDispatcher, block)
    }

    suspend fun shortLink(url: String): String = httpClient.get(url)
        .request
        .url
        .toString()

    suspend fun submitSomeData(
        aid: Long,
        bvid: String,
        mid: Long,
        upName: String,
        tName: String,
        copy: Int,
        userName: String?,
        userId: Long?
    ) {
        httpClient.get(BiliBiliAsApi.appAddAsVideoData) {
            parameter("Aid", aid)
            parameter("Bvid", bvid)
            parameter("Mid", mid)
            parameter("Upname", upName)
            parameter("Tname", tName)
            parameter("Copyright", copy)
            parameter("UserName", userName)
            parameter("UserUID", userId)
        }
    }

    // 检验token
    suspend fun checkToken() = also {
        if (key == null) {
            val userNavDataModel = getUserNavInfo()
            setKey(userNavDataModel)
        }
    }
}
