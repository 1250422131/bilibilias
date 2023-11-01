package com.imcys.network.configration

import io.ktor.client.plugins.logging.Logger
import timber.log.Timber
import javax.inject.Inject

class LoggerManager @Inject constructor() : Logger {
    override fun log(message: String) {
        Timber.tag("bilibili-as").d(message)
    }
}
