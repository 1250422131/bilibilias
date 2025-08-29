package com.imcys.bilibilias.core.datasource.api

import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.datasource.model.BilibiliNavigationData
import com.imcys.bilibilias.core.datasource.model.UserProfile
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.datasource.utils.WbiSign
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import com.imcys.bilibilias.core.logging.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.CookieEncoding
import io.ktor.http.HttpHeaders
import io.ktor.http.decodeCookieValue
import io.ktor.http.parseClientCookiesHeader
import io.ktor.http.parseQueryString
import io.ktor.http.renderSetCookieHeader
import kotlinx.coroutines.flow.first

class BilibiliApi(
    private val client: HttpClient,
    private val cookieJarDataSource: CookieJarDataSource,
    private val preferencesDataSource: AsPreferencesDataSource,
    private val logger: Logger
) {
    suspend fun getVideoInfoDetail(bvid: String): BiliVideoData {
        return client.get("/x/web-interface/view") {
            parameter("bvid", bvid)
        }.body<BiliVideoData>()
    }

    suspend fun getNavigationData(): BilibiliNavigationData {
        return client.get("/x/web-interface/nav").body<BilibiliNavigationData>()
    }

    suspend fun getPlayUrl(bvid: String, cid: Long): VideoPlaybackInfo {
        val preferences = preferencesDataSource.userData.first()
        val queryParams = mutableMapOf(
            "fnver" to 0,
            "fnval" to 4048,
            "fourk" to 1,
            "bvid" to bvid,
            "cid" to cid,
            "voice_balance" to 1,
            "gaia_source" to "pre-load",
            "isGaiaAvoided" to true,
            "web_location" to 1315873,
        ).apply {
            if (preferences.enableTryLook) {
                put("try_look", 1)
            }
        }
        val signedQuery = WbiSign.enc(queryParams)
        return client.get("/x/player/wbi/playurl") {
            url {
                encodedParameters.appendAll(parseQueryString(signedQuery))
            }
        }.body<VideoPlaybackInfo>()
    }

    suspend fun getUserProfile(cookieText: String? = null): UserProfile {
        return client.get("x/member/web/account") {
            cookieText?.let {
                parseClientCookiesHeader(it)
                    .forEach {
                        headers[HttpHeaders.Cookie] =
                            headers[HttpHeaders.Cookie] + "; " + renderSetCookieHeader(
                                it.key,
                                decodeCookieValue(it.value, CookieEncoding.URI_ENCODING),
                                CookieEncoding.RAW
                            )
                    }
            }
        }.body<UserProfile>()
    }

    suspend fun setCookieFromSetCookieHeader(cookieText: String) {
        if (cookieText.isBlank()) {
            logger.debug { "setCookie called with blank cookieText. Nothing to parse." }
            return
        }
        parseClientCookiesHeader(cookieText)
            .forEach { (name, encodedValue) ->
                cookieJarDataSource.add(
                    name,
                    decodeCookieValue(encodedValue, CookieEncoding.URI_ENCODING),
                )
            }
    }
}