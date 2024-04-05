package com.imcys.bilibilias.common.base.extend

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT

/**
 * Html化
 * @receiver String 待Html解析的字符串
 * @return String Html解析后的字符串
 */

fun String.toHtml(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, FROM_HTML_MODE_COMPACT).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

fun String.toColorInt(): Int = Color.parseColor(this)


/**
 * 取中间字符串，需要提供开始截取的位置和结束截取的位置
 * @receiver String
 * @param startString String
 * @param endString String
 * @return String
 */
fun String.extract(startString: String, endString: String): String {
    return this.substring(
        this.indexOf(startString) + startString.length,
        this.indexOf(endString)
    )
}

/**
 * 下载文件名称获取
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

    val downloadName = replace("{AV}", avid)
        .replace("{BV}", bvid)
        .replace("{P_TITLE}", pTitle)
        .replace("{CID}", cid)
        .replace("{FILE_TYPE}", fileType)
        .replace("{P}", p)
        .replace("{TITLE}", title)
        .replace("{TYPE}", type)
        .replace("\\s".toRegex(), "_")
        .replace("\\n".toRegex(), "_")

    return "/storage/emulated/0/Android/data/com.imcys.bilibilias/files/download/$downloadName"
}

/**
 * 转换为FFmpeg命令
 * @receiver String
 * @param videoPath String
 * @param audioPath String
 * @param videoMergePath String
 * @return String
 */
fun String.toAsFFmpeg(
    videoPath: String,
    audioPath: String,
    videoMergePath: String,
): Array<String> {
    val cmd =
        this.replace("{VIDEO_PATH}", videoPath).run {
            replace("{AUDIO_PATH}", audioPath)
        }.run {
            replace("{VIDEO_MERGE_PATH}", videoMergePath)
        }
    return cmd.split(" ").toTypedArray()
}
