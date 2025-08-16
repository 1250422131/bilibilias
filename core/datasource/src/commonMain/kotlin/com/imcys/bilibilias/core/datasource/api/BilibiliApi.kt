package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.datasource.model.BilibiliNavigationData
import com.imcys.bilibilias.core.datasource.model.UserProfile
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.datasource.utils.ApiResponseUnwrapper
import com.imcys.bilibilias.core.datasource.utils.WbiSign
import com.imcys.bilibilias.core.json.HttpClientJson
import com.imcys.bilibilias.core.ktor.client.createHttpClient
import com.imcys.bilibilias.core.logging.logger
import io.ktor.client.call.body
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.parseQueryString
import io.ktor.serialization.kotlinx.json.json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object BilibiliApi : KoinComponent {
    private val logger = logger<BilibiliApi>()
    private val client = createHttpClient {
        defaultRequest {
            url("https://api.bilibili.com")
            header(HttpHeaders.Origin, "https://m.bilibili.com")
            header(HttpHeaders.Referrer, "https://m.bilibili.com")
        }
        install(HttpCookies) {
            storage = get<CookiesStorage>()
        }
        install(ApiResponseUnwrapper)
        install(ContentNegotiation) {
            json(HttpClientJson)
        }
        BrowserUserAgent()
        Logging {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    this@BilibiliApi.logger.info { message }
                }
            }
        }
    }

    suspend fun getVideoInfoDetail(bvid: String): BiliVideoData {
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
        return client.get("/x/player/wbi/playurl") {
            url {
                encodedParameters.appendAll(parseQueryString(signedQuery))
            }
        }.body<VideoPlaybackInfo>()
    }

    suspend fun getUserProfile(cookieText: String? = null): UserProfile {
        return client.get("x/member/web/account") {
            header(HttpHeaders.Cookie, cookieText)
        }.body<UserProfile>()
    }

    suspend fun getVideoDetailAndPlayInfo(bvid: String): Pair<BiliVideoData, VideoPlaybackInfo> {
        val detail = getVideoInfoDetail(bvid)
        val playInfo = getPlayUrl(detail.bvid, detail.cid)
        return detail to playInfo
    }
}
//buvid3=12FC9E28-24CD-75B2-DC55-61AE5CB23E1326807infoc; b_nut=1752841526; _uuid=108F52B89-7314-5896-4D77-F83101DA9C6FB30308infoc; enable_web_push=DISABLE; buvid_fp=c5218e5358636bf120bb385e10943a39; SESSDATA=8298c290%2C1768393624%2C07199%2A71CjBBwIyv9y_EsiwLxmRSGg15RhT260lhNGQoP9_37fqLZgWVMnAhspcUhjHSKC9lIiASVlJ2Q3ZrWlFTSjVndmdhYWxnNmJrWjZzdGpyT2I0bEJ5SE5Zbi0xLXA3RllSWWZVb0hxN2JSdlRObTd5UFVLREtLUXlkWVBvZkN4OXFrZVJhcE10M0NRIIEC; bili_jct=d16030de8cdad7e025232ef0c21eb57c; DedeUserID=10993030; DedeUserID__ckMd5=70b078dc71b5cc07; theme-tip-show=SHOWED; rpdid=|(u)YYuJYu|R0J'u~lk)RuYRk; theme-avatar-tip-show=SHOWED; buvid4=C6D43028-F50A-EA7B-DAC2-E7A3E5237C2542820-023061123-Pk1O31qDhl7wEBZ8St7roNizDu1IILwOiAFjZS4zg8Mxotk7sLpx7Q%3D%3D; CURRENT_QUALITY=80; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NTU0Mzc3NDQsImlhdCI6MTc1NTE3ODQ4NCwicGx0IjotMX0.LZd79OzPxcoueVU5_630eg6SfQpE_Uquc406zznpttQ; bili_ticket_expires=1755437684; home_feed_column=5; browser_resolution=1871-1002; sid=8hc90gb6; CURRENT_FNVAL=4048; bp_t_offset_10993030=1101709461325611008; b_lsid=46DD35FC_198B310529C