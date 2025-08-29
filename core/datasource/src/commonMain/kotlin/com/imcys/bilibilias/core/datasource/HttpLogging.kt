package com.imcys.bilibilias.core.datasource

import com.imcys.bilibilias.core.logging.logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging

internal fun HttpClientConfig<*>.HttpLogging() {
    val httpLogger = logger("BilibiliApi")
    Logging {
        level = LogLevel.INFO
        logger = object : Logger {
            override fun log(message: String) {
                httpLogger.info { message }
            }
        }
    }
}