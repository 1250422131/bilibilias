package com.imcys.bilibilias.core.network.utils

import io.ktor.client.request.HttpRequestBuilder

public fun HttpRequestBuilder.parameterUpMid(mid: Long) =
    url.parameters.append("up_mid", mid.toString())