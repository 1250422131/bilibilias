package com.imcys.bilibilias.core.network.utils

import io.ktor.client.request.HttpRequestBuilder

internal fun HttpRequestBuilder.parameterUpMid(mid: Long) =
    url.parameters.append("up_mid", mid.toString())

internal fun HttpRequestBuilder.parameterBVid(bvid: String) =
    url.parameters.append("bvid", bvid)

internal fun HttpRequestBuilder.parameterAid(aid: Long) =
    url.parameters.append("aid", aid.toString())

internal fun HttpRequestBuilder.parameterAid(aid: String) =
    url.parameters.append("aid", aid)

internal fun HttpRequestBuilder.parameterMid(mid: Long) =
    url.parameters.append("mid", mid.toString())




