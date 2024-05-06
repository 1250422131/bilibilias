package com.imcys.bilibilias.core.download

import dev.DevUtils
import java.io.File

internal fun DownloadRequest.buildFullPath() =
    "${buildBasePath()}${File.separator}${format.quality}"

internal fun DownloadRequest.buildBasePath(): String {
    return "${DevUtils.getContext().downloadDir}${File.separator}${viewInfo.aid}${File.separator}c_${viewInfo.cid}"
}
