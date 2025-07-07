package com.imcys.bilibilias.core.json

import kotlinx.serialization.json.Json

val DataStoreJson = Json {
    ignoreUnknownKeys = true
    prettyPrint = false
    allowSpecialFloatingPointValues = true
}

val HttpClientJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
}