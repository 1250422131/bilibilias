package com.imcys.bilibilias.core.datasource.utils

import com.imcys.bilibilias.core.datasource.api.BilibiliApi.getNavigationData
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.core.startup.Startup
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class WbiInitializer : Startup {
    private val logger = logger<WbiInitializer>()
    override suspend fun initialize() {
        var attempt = 0
        while (attempt < 5)
            try {
                val data = getNavigationData()
                val wbiImg = data.wbiImg
                WbiSign.initializeMixinKey(wbiImg.imgUrl, wbiImg.subUrl)
                logger.info { "WBI initialized successfully." }
                break
            } catch (e: Exception) {
                attempt++
                delay(1.seconds)
                logger.error(e) { "Error initializing WBI (attempt $attempt)." }
            }
    }
}