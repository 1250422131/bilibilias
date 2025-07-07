package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.createHttpClient
import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.datasource.model.BilibiliNavigationData
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.datasource.persistent.CookiesStorageImpl
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.datasource.utils.WbiSign
import com.imcys.bilibilias.core.json.HttpClientJson
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object BilibiliApi {
    private val client = createHttpClient {
        defaultRequest {
            url("https://api.bilibili.com")
            header(HttpHeaders.Origin, "https://m.bilibili.com")
            header(HttpHeaders.Referrer, "https://m.bilibili.com")
        }
        install(HttpCookies) {
            storage = CookiesStorageImpl
        }
        install(ApiResponseUnwrapper)
        install(ContentNegotiation) {
            json(HttpClientJson)
        }
        Logging {

        }
    }

    init {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            val data = getNavigationData()
            val wbiImg = data.wbiImg
            WbiSign.initializeMixinKey(wbiImg.imgUrl, wbiImg.subUrl)
        }
    }

    suspend fun getVideoDetail(bvid: String): BiliVideoData {
        return client.get("/x/web-interface/view") {
            parameter("bvid", bvid)
        }.body<BiliVideoData>()
    }

    suspend fun getNavigationData(): BilibiliNavigationData {
        return client.get("/x/web-interface/nav").body<BilibiliNavigationData>()
    }

    suspend fun getPlayUrl(bvid: String, cid: Long): VideoPlaybackInfo {
        val queryParams = mapOf(
            PlayUrlConstants.PARAM_FNVER to PlayUrlConstants.FNVER_VALUE,
            PlayUrlConstants.PARAM_FNVAL to PlayUrlConstants.FNVAL_VALUE,
            PlayUrlConstants.PARAM_FOURK to PlayUrlConstants.FOURK_VALUE,
            PlayUrlConstants.PARAM_BVID to bvid,
            PlayUrlConstants.PARAM_CID to cid,
        )
        val signedQuery = WbiSign.enc(queryParams)
        return client.get("/x/player/wbi/playurl?$signedQuery").body<VideoPlaybackInfo>()
    }

    private object PlayUrlConstants {
        const val FNVER_VALUE = 0
        const val FNVAL_VALUE = 4048
        const val FOURK_VALUE = 1
        const val PARAM_FNVER = "fnver"
        const val PARAM_FNVAL = "fnval"
        const val PARAM_FOURK = "fourk"
        const val PARAM_BVID = "bvid"
        const val PARAM_CID = "cid"
    }
}
