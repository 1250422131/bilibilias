package com.imcys.bilibilias.core.json

import kotlinx.serialization.json.Json

val HttpClientJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}