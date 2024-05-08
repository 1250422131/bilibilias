package com.imcys.bilibilias.common.base.extend

import android.graphics.Color

fun String.toColorInt(): Int = Color.parseColor(this)

/**
 * bilibilias下载文件名称获取
 */
fun String.toAsDownloadSavePath(
    avid: String = "",
    bvid: String = "",
    pTitle: String = "",
    cid: String = "",
    fileType: String = "",
    p: String = "",
    title: String = "",
    type: String = "",
): String {
    val savePath = "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download"

    var downloadName =
        this.replace("{AV}", avid)
    downloadName = downloadName.replace("{BV}", bvid)
    downloadName =
        downloadName.replace("{P_TITLE}", pTitle)
    downloadName = downloadName.replace("{CID}", cid)
    downloadName = downloadName.replace("{FILE_TYPE}", fileType)
    downloadName = downloadName.replace("{P}", p)
    downloadName = downloadName.replace(
        "{TITLE}",
        title
    )
    downloadName = downloadName.replace("{TYPE}", type)
    downloadName = downloadName.replace("""\s""".toRegex(), "_")
    downloadName = downloadName.replace("""\n""".toRegex(), "_")
    downloadName = downloadName.replace("""\\""".toRegex(), "_")

    return "$savePath/$downloadName"
}
