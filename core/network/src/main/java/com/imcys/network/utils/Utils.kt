package com.imcys.network.utils

import com.imcys.network.constants.BILIBILI_URL
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

fun HttpRequestBuilder.parameterBV(bvid: String): Unit = url.parameters.append("bvid", bvid)
fun HttpMessageBuilder.headerRefBilibili(): Unit = headers.append(HttpHeaders.Referrer, BILIBILI_URL)
