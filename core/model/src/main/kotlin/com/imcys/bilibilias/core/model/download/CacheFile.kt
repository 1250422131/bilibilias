package com.imcys.bilibilias.core.model.download

import com.imcys.bilibilias.core.model.video.ViewInfo
import java.io.File

data class CacheFile(
    val viewInfo: ViewInfo,
    val vFile: File?,
    val aFile: File?,
    val dFile: File?,
)
