package com.imcys.bilibilias.common.base.extend

import android.content.Context
import android.graphics.Color
import android.graphics.Path
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.core.graphics.toColorInt
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils

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
    return this.substring(this.indexOf(startString) + startString.length,
        this.indexOf(endString))
}

/**
 * bilibilias下载文件名称获取
 * @receiver String
 * @param avid String
 * @param bvid String
 * @param pTitle String
 * @param cid String
 * @param fileType String
 * @param p String
 * @param title String
 * @param type String
 * @return String
 */
fun String.toAsDownloadSavePath(
    context: Context,
    avid: String = "",
    bvid: String = "",
    pTitle: String = "",
    cid: String = "",
    fileType: String = "",
    p: String = "",
    title: String = "",
    type: String = "",
): String {

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var savePath = sharedPreferences.getString("user_download_save_path",
        context.getExternalFilesDir("download").toString())

    val sdPathState =
        sharedPreferences.getBoolean("user_download_save_sd_path_switch",
            false)

    //获取下载地址
    if (sdPathState) {
        savePath = "${
            AppFilePathUtils(
                context,
                "com.imcys.bilibilias").sdCardDirectory
        }/Android/data/com.imcys.bilibilias/files/download"
    }

    var downloadName =
        this.replace("{AV}", avid)
    downloadName = downloadName.replace("{BV}", bvid)
    downloadName =
        downloadName.replace("{P_TITLE}", pTitle)
    downloadName = downloadName.replace("{CID}", cid)
    downloadName = downloadName.replace("{FILE_TYPE}", fileType)
    downloadName = downloadName.replace("{P}", p)
    downloadName = downloadName.replace("{TITLE}",
        title)
    downloadName = downloadName.replace("{TYPE}", type)
    downloadName = downloadName.replace(" ", "_")

    return "${savePath}/$downloadName"
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



