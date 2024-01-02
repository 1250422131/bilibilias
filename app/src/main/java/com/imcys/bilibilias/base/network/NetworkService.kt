package com.imcys.bilibilias.base.network

import com.imcys.bilibilias.base.model.login.LoginQrcodeBean
import com.imcys.bilibilias.base.model.login.LoginStateBean
import com.imcys.bilibilias.base.model.user.LikeVideoBean
import com.imcys.bilibilias.base.model.user.UserInfoBean
import com.imcys.bilibilias.common.base.api.BiliBiliAsApi
import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.COOKIE
import com.imcys.bilibilias.common.base.constant.COOKIES
import com.imcys.bilibilias.common.base.constant.ROAM_API
import com.imcys.bilibilias.common.base.model.user.AsUserLoginModel
import com.imcys.bilibilias.common.base.model.user.BiLiCookieResponseModel
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.model.user.ResponseResult
import com.imcys.bilibilias.common.base.model.user.UserBiliBiliCookieModel
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
import com.imcys.bilibilias.home.ui.model.BangumiPlayBean
import com.imcys.bilibilias.home.ui.model.BangumiSeasonBean
import com.imcys.bilibilias.home.ui.model.CollectionDataBean
import com.imcys.bilibilias.home.ui.model.CollectionResultBean
import com.imcys.bilibilias.home.ui.model.DashBangumiPlayBean
import com.imcys.bilibilias.home.ui.model.DashVideoPlayBean
import com.imcys.bilibilias.home.ui.model.DonateViewBean
import com.imcys.bilibilias.home.ui.model.OldDonateBean
import com.imcys.bilibilias.home.ui.model.OldToolItemBean
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
import com.imcys.bilibilias.home.ui.viewmodel.AsLoginBsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(
    private val ktHttpUtils: KtHttpUtils,
    private val httpClient: HttpClient
) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun n1(cid: Long, qn: Int): DashBangumiPlayBean = withContext(ioDispatcher) {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?cid=$cid&qn=$qn&fnval=4048&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n2(bvid: String, cid: Long, qn: Int): DashVideoPlayBean =
        withContext(ioDispatcher) { videoPlayPath(bvid, cid.toString(), qn) }

    suspend fun n10(bvid: String, cid: Long): DashVideoPlayBean = withContext(ioDispatcher) {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    suspend fun n29(bvid: String, cid: Long): DashVideoPlayBean = withContext(ioDispatcher) {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    suspend fun n30(bvid: String, cid: Long): DashVideoPlayBean = withContext(ioDispatcher) {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    private suspend inline fun <reified T> videoPlayPath(
        bvid: String,
        cid: String,
        qn: Int,
        fnval: Int = 4048,
    ): T = withContext(ioDispatcher) {
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
    suspend fun n3(bvid: String, cid: Long, qn: Int): VideoPlayBean = withContext(ioDispatcher) {
        videoPlayPath(bvid, cid.toString(), qn, fnval = 0)
    }

    suspend fun n9(bvid: String, cid: Long): VideoPlayBean = withContext(ioDispatcher) {
        videoPlayPath(bvid, cid.toString(), 64, fnval = 0)
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n4(cid: Long, qn: Int): BangumiPlayBean = withContext(ioDispatcher) {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?cid=$cid&qn=$qn&fnval=0&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    suspend fun n16(epid: Long): BangumiPlayBean = withContext(ioDispatcher) {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?ep_id=$epid&qn=64&fnval=0&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n5(bvid: String): VideoBaseBean = withContext(ioDispatcher) {
        n12(bvid)
    }

    suspend fun n12(bvid: String): VideoBaseBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getVideoDataPath) { parameterBVID(bvid) }.body()
    }

    suspend fun n26(bvid: String): VideoBaseBean = withContext(ioDispatcher) {
        n12(bvid)
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n6(): MyUserData = withContext(ioDispatcher) {
        n38()
    }

    suspend fun n27(): MyUserData = withContext(ioDispatcher) {
        n38()
    }

    suspend fun biliUserLogin(qrcodeKey: String): LoginStateBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getLoginStatePath + "?qrcode_key=" + qrcodeKey).body()
    }

    suspend fun getLoginQRData(): LoginQrcodeBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getLoginQRPath).body()
    }

    suspend fun getMyUserData(): MyUserData = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    suspend fun getDanmuBytes(cid: Long) = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.videoDanMuPath}?oid=$cid") {
            refererBILIHarder()
        }.readBytes()
    }

    suspend fun getOldToolItem(): OldToolItemBean = withContext(ioDispatcher) {
        httpClient.get("${BiliBiliAsApi.appFunction}?type=oldToolItem").body()
    }

    suspend fun getUserCollection(id: Long, pn: Int): CollectionDataBean =
        withContext(ioDispatcher) {
            httpClient.get("${BilibiliApi.userCollectionDataPath}?media_id=${id}&pn=${pn}&ps=20")
                .body()
        }

    suspend fun getDonateData(): OldDonateBean = withContext(ioDispatcher) {
        httpClient.get("${BiliBiliAsApi.appFunction}?type=Donate").body()
    }

    suspend fun n38(): MyUserData = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n7(epid: Int): BangumiSeasonBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.bangumiVideoDataPath) {
            parameterEpID(epid.toString())
        }.body()
    }

    suspend fun n13(epid: Long): BangumiSeasonBean = withContext(ioDispatcher) {
        httpClient.get(ROAM_API + "pgc/view/web/season?ep_id=" + epid).body()
    }

    suspend fun n18(firstEp: Int): BangumiSeasonBean = withContext(ioDispatcher) {
        n7(firstEp)
    }

    suspend fun n25(epId: Long): BangumiSeasonBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epId").body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n8(): UserNavDataModel = withContext(ioDispatcher) {
        httpClient.get("https://api.bilibili.com/x/web-interface/nav").body()
    }

    suspend fun n40(): UserNavDataModel = withContext(ioDispatcher) {
        n42()
    }

    suspend fun n42(): UserNavDataModel = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.userNavDataPath).body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n11(paramsStr: String): UserBaseBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userBaseDataPath}?$paramsStr").body()
    }

    suspend fun n24(paramsStr: String): UserBaseBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userBaseDataPath}?$paramsStr").body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n14(mid: Long): UserCardBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.getUserCardPath) {
            parameterMID(mid.toString())
        }.body()
    }

    suspend fun n22(paramsStr: String): UserCardBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userWorksPath}?$paramsStr").body()
    }

    // ---------------------------------------------------------------------------------------------

    suspend fun n17(): UserCreateCollectionBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.userCreatedScFolderPath) {
            parameterUpMID(BaseApplication.asUser.mid.toString())
        }.body()
    }

    suspend fun n34(): UserCreateCollectionBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.userCreatedScFolderPath) {
            parameterUpMID(BaseApplication.asUser.mid.toString())
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n15(bvid: String): VideoPageListData = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.videoPageListPath) {
            parameterBVID(bvid)
        }.body()
    }
    // ---------------------------------------------------------------------------------------------
    /**
     * [n20] [n21]
     */
    suspend fun n19(paramsStr: String): UserWorksBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userWorksPath}?$paramsStr").body()
    }

    suspend fun n20(i: Int): UserWorksBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi.userWorksPath) {
            parameterMID(BaseApplication.asUser.mid.toString())
            parameter("pn", i)
            parameter("ps", 20)
        }.body()
    }

    suspend fun n21(paramsStr: String): UserWorksBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userWorksPath}?$paramsStr").body()
    }

    // ----------------------------------------------------------------------------------------------
    suspend fun n23(): UpStatBeam = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.userUpStat}?mid=${BaseApplication.asUser.mid}").body()
    }

    suspend fun n28(paramsStr: String): UserInfoBean = withContext(ioDispatcher) {
        httpClient.get("${BilibiliApi.getUserInfoPath}?$paramsStr").body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n31(bvid: String): LikeVideoBean = withContext(ioDispatcher) {
        ktHttpUtils.addHeader(
            COOKIE,
            BaseApplication.dataKv.decodeString(COOKIES, "")!!,
        )
            .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
            .addParam("like", "1")
            .addParam("bvid", bvid)
            .asyncPost(BilibiliApi.videLikePath)
    }

    suspend fun n32(bvid: String): LikeVideoBean = withContext(ioDispatcher) {
        ktHttpUtils.addHeader(
            COOKIE,
            BaseApplication.dataKv.decodeString(COOKIES, "")!!,
        )
            .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "") ?: "")
            .addParam("like", "2")
            .addParam("bvid", bvid)
            .asyncPost(BilibiliApi.videLikePath)
    }

    suspend fun n33(bvid: String): VideoCoinAddBean = withContext(ioDispatcher) {
        ktHttpUtils
            .addHeader(COOKIE, BaseApplication.dataKv.decodeString(COOKIES, "")!!)
            .addParam("bvid", bvid)
            .addParam("multiply", "2")
            .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
            .asyncPost(BilibiliApi.videoCoinAddPath)
    }

    suspend fun n35(toString: String, addMediaIds: String): CollectionResultBean =
        withContext(ioDispatcher) {
            httpClient.get(BilibiliApi.videoCollectionSetPath) {
                parameter("rid", toString)
                parameter("add_media_ids", addMediaIds)
                parameter("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
                parameter("type", "2")
            }.body()
        }

    suspend fun n36(
        asCookie: String?,
        asLoginInfo: AsLoginBsViewModel.AsLoginInfo
    ): AsUserLoginModel =
        withContext(ioDispatcher) {
            ktHttpUtils.addHeader(COOKIE, asCookie!!).asyncPostJson(
                "${BiliBiliAsApi.serviceTestApi}users/login",
                asLoginInfo,
            )
        }

    suspend fun n37(asCookie: String?): UserBiliBiliCookieModel = withContext(ioDispatcher) {
        ktHttpUtils.addHeader(COOKIE, asCookie!!)
            .asyncGet("${BiliBiliAsApi.serviceTestApi}BiliBiliCookie")
    }

    suspend fun n39(asCookie: String?, data: UserBiliBiliCookieModel.Data): ResponseResult<Any> =
        withContext(ioDispatcher) {
            ktHttpUtils.addHeader(COOKIE, asCookie!!).asyncDeleteJson(
                "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                data,
            )
        }

    suspend fun n41(): LoginQrcodeBean =
        withContext(ioDispatcher) { ktHttpUtils.asyncGet(BilibiliApi.getLoginQRPath) }

    suspend fun n43(biliBiliCookieInfo: AsLoginBsViewModel.BiliBiliCookieInfo): BiLiCookieResponseModel =
        withContext(ioDispatcher) {
            ktHttpUtils.addHeader(COOKIE, BaseApplication.asUser.asCookie)
                .asyncPostJson(
                    "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                    biliBiliCookieInfo,
                )
        }

    suspend fun n44() = withContext(ioDispatcher) {}
    suspend fun n45() = withContext(ioDispatcher) {}
    suspend fun n46() = withContext(ioDispatcher) {}
    suspend fun n47() = withContext(ioDispatcher) {}
    suspend fun n48() = withContext(ioDispatcher) {}
    suspend fun n49() = withContext(ioDispatcher) {}
    suspend fun n50() = withContext(ioDispatcher) {}
    suspend fun n51() = withContext(ioDispatcher) {}
}
