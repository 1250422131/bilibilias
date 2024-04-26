package com.imcys.bilibilias.core.download

import dev.DevUtils
import java.io.File

fun DownloadRequest.buildFullPath() =
    "${buildBasePath()}${File.separator}${format.quality}"

fun DownloadRequest.buildBasePath(): String {
    return "${DevUtils.getContext().downloadDir}${File.separator}${viewInfo.aid}${File.separator}c_${viewInfo.cid}"
}
