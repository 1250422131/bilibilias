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
import com.imcys.bilibilias.common.base.model.common.BangumiFollowList
import com.imcys.bilibilias.common.base.model.user.AsUserLoginModel
import com.imcys.bilibilias.common.base.model.user.BiLiCookieResponseModel
import com.imcys.bilibilias.common.base.model.user.MyUserData
import com.imcys.bilibilias.common.base.model.user.ResponseResult
import com.imcys.bilibilias.common.base.model.user.UserBiliBiliCookieModel
import com.imcys.bilibilias.common.base.utils.http.KtHttpUtils
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
import com.imcys.bilibilias.home.ui.viewmodel.AsLoginBsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(
        private val ktHttpUtils: KtHttpUtils,
        private val httpClient: HttpClient,
        private val asCookiesStorage: AsCookiesStorage
) {
    private val ioDispatcher = Dispatchers.IO
    suspend fun n1(cid: Long, qn: Int): DashBangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?cid=$cid&qn=$qn&fnval=4048&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n2(bvid: String, cid: Long, qn: Int): DashVideoPlayBean =
            runCatchingOnWithContextIo { videoPlayPath(bvid, cid.toString(), qn) }

    suspend fun n10(bvid: String, cid: Long): DashVideoPlayBean = runCatchingOnWithContextIo {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    suspend fun n29(bvid: String, cid: Long): DashVideoPlayBean = runCatchingOnWithContextIo {
        videoPlayPath(bvid, cid.toString(), 64)
    }

    suspend fun getDashBangumiPlay(cid: Long, qn: Int): DashBangumiPlayBean =
            runCatchingOnWithContextIo {
                httpClient.get("${ROAM_API}pgc/player/web/playurl?cid=$cid&qn=$qn&fnval=4048&fourk=1") {
                    refererBILIHarder()
                }.body()
            }

    suspend fun getPlayHistory(max: Long, viewAt: Long, type: String): PlayHistoryBean =
            runCatchingOnWithContextIo {
                httpClient.get("${BilibiliApi.userPlayHistoryPath}?max=$max&view_at=$viewAt&type=archive")
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

    suspend fun n9(bvid: String, cid: Long): VideoPlayBean = runCatchingOnWithContextIo {
        videoPlayPath(bvid, cid.toString(), 64, fnval = 0)
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n4(cid: Long, qn: Int): BangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?cid=$cid&qn=$qn&fnval=0&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    suspend fun n16(epid: Long): BangumiPlayBean = runCatchingOnWithContextIo {
        httpClient.get("${ROAM_API}pgc/player/web/playurl?ep_id=$epid&qn=64&fnval=0&fourk=1") {
            refererBILIHarder()
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n5(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        n12(bvid)
    }

    suspend fun n12(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getVideoDataPath) { parameterBVID(bvid) }.body()
    }

    suspend fun n26(bvid: String): VideoBaseBean = runCatchingOnWithContextIo {
        n12(bvid)
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n6(): MyUserData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    suspend fun n27(): MyUserData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    suspend fun getBILIHome(): String = runCatchingOnWithContextIo {
        httpClient.get(BILIBILI_URL).body()
    }

    suspend fun biliUserLogin(qrcodeKey: String): LoginStateBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getLoginStatePath + "?qrcode_key=" + qrcodeKey).body()
    }

    suspend fun getLoginQRData(): LoginQrcodeBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getLoginQRPath).body()
    }

    suspend fun getMyUserData(): MyUserData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    suspend fun getDanmuBytes(cid: Long) = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.videoDanMuPath}?oid=$cid") {
            refererBILIHarder()
        }.readBytes()
    }

    suspend fun getOldToolItem(): OldToolItemBean = runCatchingOnWithContextIo {
        httpClient.get("${BiliBiliAsApi.appFunction}?type=oldToolItem").body()
    }

    suspend fun getUserCollection(id: Long, pn: Int): CollectionDataBean =
            runCatchingOnWithContextIo {
                httpClient.get("${BilibiliApi.userCollectionDataPath}?media_id=${id}&pn=${pn}&ps=20")
                        .body()
            }

    suspend fun getDonateData(): OldDonateBean = runCatchingOnWithContextIo {
        httpClient.get("${BiliBiliAsApi.appFunction}?type=Donate").body()
    }

    suspend fun getOldHomeBannerData(): OldHomeBannerDataBean = runCatchingOnWithContextIo {
        httpClient.get("${BiliBiliAsApi.updateDataPath}?type=banner").body()
    }

    suspend fun getBangumiFollow(vmid: Long, type: Int, pn: Int, ps: Int): BangumiFollowList =
            runCatchingOnWithContextIo {
                httpClient.get("${BilibiliApi.bangumiFollowPath}?vmid=$vmid&type=$type&pn=$pn&ps=$ps")
                        .body()

            }

    suspend fun getUpdateData(): OldUpdateDataBean = runCatchingOnWithContextIo {
        httpClient.get("${BiliBiliAsApi.updateDataPath}?type=json&version=${BiliBiliAsApi.version}")
                .body()
    }


    suspend fun getOldHomeAd(): OldHomeAdBean = runCatchingOnWithContextIo {
        httpClient.get("${BiliBiliAsApi.appFunction}?type=oldHomeAd").body()
    }

    suspend fun n38(): MyUserData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getMyUserData).body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n7(epid: Int): BangumiSeasonBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.bangumiVideoDataPath) {
            parameterEpID(epid.toString())
        }.body()
    }

    suspend fun n13(epid: Long): BangumiSeasonBean = runCatchingOnWithContextIo {
        httpClient.get(ROAM_API + "pgc/view/web/season?ep_id=" + epid).body()
    }

    suspend fun n18(firstEp: Int): BangumiSeasonBean = runCatchingOnWithContextIo {
        n7(firstEp)
    }

    suspend fun n25(epId: Long): BangumiSeasonBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.bangumiVideoDataPath}?ep_id=$epId").body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n8(): UserNavDataModel = runCatchingOnWithContextIo {
        httpClient.get("https://api.bilibili.com/x/web-interface/nav").body()
    }

    suspend fun n40(): UserNavDataModel = runCatchingOnWithContextIo {
        n42()
    }

    suspend fun n42(): UserNavDataModel = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userNavDataPath).body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n11(paramsStr: String): UserBaseBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.userBaseDataPath}?$paramsStr").body()
    }

    suspend fun n24(paramsStr: String): UserBaseBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.userBaseDataPath}?$paramsStr").body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n14(mid: Long): UserCardBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.getUserCardPath) {
            parameterMID(mid.toString())
        }.body()
    }

    suspend fun n22(paramsStr: String): UserCardBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.getUserCardPath}?$paramsStr").body()
    }

    // ---------------------------------------------------------------------------------------------

    suspend fun n17(): UserCreateCollectionBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userCreatedScFolderPath) {
            parameterUpMID(BaseApplication.asUser.mid.toString())
        }.body()
    }

    suspend fun n34(): UserCreateCollectionBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userCreatedScFolderPath) {
            parameterUpMID(BaseApplication.asUser.mid.toString())
        }.body()
    }

    // ---------------------------------------------------------------------------------------------
    suspend fun n15(bvid: String): VideoPageListData = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.videoPageListPath) {
            parameterBVID(bvid)
        }.body()
    }
    // ---------------------------------------------------------------------------------------------
    /**
     * [n20] [n21]
     */
    suspend fun n19(paramsStr: String): UserWorksBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.userWorksPath}?$paramsStr").body()
    }

    suspend fun n20(i: Int): UserWorksBean = runCatchingOnWithContextIo {
        httpClient.get(BilibiliApi.userWorksPath) {
            parameterMID(BaseApplication.asUser.mid.toString())
            parameter("pn", i)
            parameter("ps", 20)
        }.body()
    }

    suspend fun n21(paramsStr: String): UserWorksBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.userWorksPath}?$paramsStr").body()
    }

    // ----------------------------------------------------------------------------------------------
    suspend fun n23(): UpStatBeam = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.userUpStat}?mid=${BaseApplication.asUser.mid}").body()
    }

    suspend fun n28(paramsStr: String): UserInfoBean = runCatchingOnWithContextIo {
        httpClient.get("${BilibiliApi.getUserInfoPath}?$paramsStr").body()
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
        ktHttpUtils.addHeader(
                COOKIE,
                BaseApplication.dataKv.decodeString(COOKIES, "")!!,
        )
                .addParam("csrf", asCookiesStorage.getCookieValue("bili_jct") ?: "")
                .addParam("like", "2")
                .addParam("bvid", bvid)
                .asyncPost(BilibiliApi.videLikePath)
    }

    suspend fun n33(bvid: String): VideoCoinAddBean = runCatchingOnWithContextIo {
        ktHttpUtils
                .addHeader(COOKIE, asCookiesStorage.getCookieValue("bili_jct") ?: "")
                .addParam("bvid", bvid)
                .addParam("multiply", "2")
                .addParam("csrf", BaseApplication.dataKv.decodeString("bili_jct", "")!!)
                .asyncPost(BilibiliApi.videoCoinAddPath)
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


    suspend fun n36(
            asCookie: String?,
            asLoginInfo: AsLoginBsViewModel.AsLoginInfo
    ): AsUserLoginModel =
            runCatchingOnWithContextIo {
                ktHttpUtils.addHeader(COOKIE, asCookie!!).asyncPostJson(
                        "${BiliBiliAsApi.serviceTestApi}users/login",
                        asLoginInfo,
                )
            }

    suspend fun n37(asCookie: String?): UserBiliBiliCookieModel = runCatchingOnWithContextIo {
        ktHttpUtils.addHeader(COOKIE, asCookie!!)
                .asyncGet("${BiliBiliAsApi.serviceTestApi}BiliBiliCookie")
    }

    suspend fun n39(asCookie: String?, data: UserBiliBiliCookieModel.Data): ResponseResult =
            runCatchingOnWithContextIo {
                ktHttpUtils.addHeader(COOKIE, asCookie!!).asyncDeleteJson(
                        "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                        data,
                )
            }

    suspend fun n41(): LoginQrcodeBean =
            runCatchingOnWithContextIo { ktHttpUtils.asyncGet(BilibiliApi.getLoginQRPath) }

    suspend fun n43(biliBiliCookieInfo: AsLoginBsViewModel.BiliBiliCookieInfo): BiLiCookieResponseModel =
            runCatchingOnWithContextIo {

                ktHttpUtils.addHeader(COOKIE, BaseApplication.asUser.asCookie)
                        .asyncPostJson(
                                "${BiliBiliAsApi.serviceTestApi}BiliBiliCookie",
                                biliBiliCookieInfo,
                        )
            }

    private suspend inline fun <reified T> runCatchingOnWithContextIo(
            noinline block: suspend CoroutineScope.() -> T
    ): T {
        return runCatching {
            withContext(ioDispatcher, block)
        }.getOrElse {
            val clazz = T::class
            val constructors = clazz.constructors
            val emptyConstructor = constructors.find { parameter -> parameter.parameters.isEmpty() }
            emptyConstructor?.call() as T ?: throw it
        }
    }


    suspend fun n44() = runCatchingOnWithContextIo {}
    suspend fun n45() = runCatchingOnWithContextIo {}
    suspend fun n46() = runCatchingOnWithContextIo {}
    suspend fun n47() = runCatchingOnWithContextIo {}
    suspend fun n48() = runCatchingOnWithContextIo {}
    suspend fun n49() = runCatchingOnWithContextIo {}
    suspend fun n50() = runCatchingOnWithContextIo {}
    suspend fun n51() = runCatchingOnWithContextIo {}
}
