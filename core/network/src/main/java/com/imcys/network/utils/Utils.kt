package com.imcys.network.utils

import com.imcys.network.constants.BILIBILI_URL
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

fun HttpRequestBuilder.parameterBV(bvid: String): Unit =
    url.parameters.append("bvid", bvid)

fun HttpMessageBuilder.headerRefBilibili(): Unit = headers.append(HttpHeaders.Referrer, BILIBILI_URL)
fun HttpRequestBuilder.parameterPlatform(platform: String = "web"): Unit =
    url.parameters.append("platform", platform)

fun HttpRequestBuilder.parameterMediaID(mediaId: Long): Unit =
    url.parameters.append("media_id", mediaId.toString())

fun HttpRequestBuilder.parameterPageNumber(page: Int): Unit =
    url.parameters.append("pn", page.toString())

fun HttpRequestBuilder.parameterPageLimit(limit: Int): Unit =
    url.parameters.append("ps", limit.toString())

fun HttpRequestBuilder.parameterMID(mid: Long): Unit =
    url.parameters.append("mid", mid.toString())