package com.imcys.network.utils

import com.imcys.network.constant.BILIBILI_WEB_URL
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

context (HttpRequestBuilder)
internal fun parameterBV(bvid: String): Unit = url.parameters.append("bvid", bvid)

internal fun HttpRequestBuilder.parameterEpId(epId: Any): Unit =
    url.parameters.append("ep_id", epId.toString())

internal fun HttpRequestBuilder.parameterPlatform(platform: String = "web"): Unit =
    url.parameters.append("platform", platform)

internal fun HttpRequestBuilder.parameterMediaID(mediaId: Long): Unit =
    url.parameters.append("media_id", mediaId.toString())

internal fun HttpRequestBuilder.parameterPN(pn: Int): Unit =
    url.parameters.append("pn", pn.toString())

internal fun HttpRequestBuilder.parameterPS(ps: Int): Unit =
    url.parameters.append("ps", ps.toString())

internal fun HttpRequestBuilder.parameterPageNum(num: Int): Unit =
    url.parameters.append("page_num", num.toString())

internal fun HttpRequestBuilder.parameterPageSize(size: Int): Unit =
    url.parameters.append("page_size", size.toString())

internal fun HttpRequestBuilder.parameterMID(mid: Long): Unit =
    url.parameters.append("mid", mid.toString())

internal fun HttpRequestBuilder.parameterCID(cId: Long): Unit =
    url.parameters.append("cid", cId.toString())

internal fun HttpRequestBuilder.parameterCSRF(csrf: String): Unit =
    url.parameters.append("csrf", csrf)
internal fun HttpRequestBuilder.parameterRefreshToken(token: String): Unit =
    url.parameters.append("refresh_token", token)
internal fun HttpRequestBuilder.parameterRefreshCsrf(csrf: String): Unit =
    url.parameters.append("refresh_csrf", csrf)

internal fun HttpMessageBuilder.headerRefBilibili(): Unit =
    headers.append(HttpHeaders.Referrer, BILIBILI_WEB_URL)
