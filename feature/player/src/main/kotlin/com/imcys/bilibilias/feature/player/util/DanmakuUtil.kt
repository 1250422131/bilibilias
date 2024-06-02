package com.imcys.bilibilias.feature.player.util

import dev.DevUtils
import java.io.File

internal object DanmakuUtil {
    private val tempDM = "temp_dm.xml"
    private val cachePath = DevUtils.getContext().cacheDir.path
    fun writer(byteArray: ByteArray) {
        File(tempDM, cachePath).writeBytes(byteArray)
    }

    fun parseFile() {

    }
}

