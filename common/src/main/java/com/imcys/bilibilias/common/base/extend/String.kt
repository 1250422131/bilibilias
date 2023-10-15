package com.imcys.bilibilias.common.base.extend

import android.content.Context
import android.graphics.Color
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.common.base.config.SettingsRepository
import com.imcys.bilibilias.common.base.utils.file.AppFilePathUtils
import java.math.BigInteger
import java.security.MessageDigest

fun String.toColorInt(): Int = Color.parseColor(this)

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

    var savePath = sharedPreferences.getString(
        "user_download_save_path",
        context.getExternalFilesDir("download").toString()
    )

    val sdPathState =
        sharedPreferences.getBoolean(
            "user_download_save_sd_path_switch",
            false
        )

    // 获取下载地址
    if (sdPathState) {
        savePath = "${
            AppFilePathUtils(
                context,
                "com.imcys.bilibilias"
            ).sdCardDirectory
        }/Android/data/com.imcys.bilibilias/files/download"
    }

    val downloadName = this.replace("{AV}", avid)
        .replace("{BV}", bvid)
        .replace("{P_TITLE}", pTitle)
        .replace("{CID}", cid)
        .replace("{FILE_TYPE}", fileType)
        .replace("{P}", p)
        .replace("{TITLE}", title)
        .replace("{TYPE}", type)
        .replace(" ", "_")

    return "$savePath/$downloadName"
}

fun String.toAsDownloadSavePath(
    bvid: String,
    pTitle: String,
    cid: String,
    fileType: String,
    p: String,
    title: String,
    type: String,
): String {
    // "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}
    // download/bvid/cid/画质/video.m4s
    val rule = SettingsRepository.videoNameRule ?: SettingsRepository.DefaultVideoNameRule
    rule.filterNot { it.isWhitespace() }
    rule.replace("{BV}",bvid)
        .replace("{FILE_TYPE}",fileType)
        .replace("{P_TITLE}",pTitle)
        .replace("{CID}",cid)
        .plus(".")
        .plus(type)
    return ""
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
    return replace("{VIDEO_PATH}", videoPath)
        .replace("{AUDIO_PATH}", audioPath)
        .replace("{VIDEO_MERGE_PATH}", videoMergePath)
        .split(' ')
        .toTypedArray()
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
