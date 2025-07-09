package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.datasource.model.BilibiliNavigationData
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.datasource.persistent.CookiesStorageImpl
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.datasource.utils.WbiSign
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
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
            level = LogLevel.BODY
            logger = object : Logger {
                override fun log(message: String) {
                    co.touchlab.kermit.Logger.i("BilibiliApi") { message }
                }
            }
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
            "fnver" to 0,
            "fnval" to 4048,
            "fourk" to 1,
            "bvid" to bvid,
            "cid" to cid,
        )
        val signedQuery = WbiSign.enc(queryParams)
        return client.get("/x/player/wbi/playurl?$signedQuery").body<VideoPlaybackInfo>()
    }
}
