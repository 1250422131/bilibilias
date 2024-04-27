package com.imcys.bilibilias.core.common.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dev.DevUtils

fun getDefaultSharedPreferences() =
    PreferenceManager.getDefaultSharedPreferences(DevUtils.getContext())

fun Context.getUserDownloadSavePath(): String =
    getDefaultSharedPreferences().getString(
        "user_download_save_path",
        getExternalFilesDir("download")?.absolutePath
    ) ?: error("获取用户下载路径错误")

fun getAppNotices(): String =
    getDefaultSharedPreferences()
        .getString("AppNotice", "") ?: error("获取 APP 更新日志错误")

fun setAppNotices(notice: String) =
    edit {
        putString("AppNotice", notice)
    }

fun getUserSetDownloadFileNameRule() = getDefaultSharedPreferences().getString(
    "user_download_file_name_editText",
    "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}",
) ?: error("获取用户设置的文件名设置错误")

fun setUserDownloadFileNameRule(text: String) {
    edit {
        putString("user_download_file_name_editText", text)
    }
}

fun restoreVideoNameRule() {
    edit {
        putString(
            "user_download_file_name_editText",
            "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}"
        )
    }
}

fun get保存路径(): String? {
    return getDefaultSharedPreferences().getString("user_download_save_uri_path", null)
}

fun set保存路径(path: String) {
    edit {
        putString("user_download_save_uri_path", path)
    }
}

fun restoreDownloadAddress() {
    edit {
        putString("user_download_save_uri_path", null)
    }
}

// TODO: 函数命名不对
fun getBiliBiliUri(): String? {
    return getDefaultSharedPreferences().getString("AppDataUri", "")
        ?: error("获取B站路径错误")
}

// TODO: 函数命名不对
fun setBiliBiliUri(path: String) {
    edit {
        putString("AppDataUri", path)
    }
}

fun 微软统计(enable: Boolean) {
    edit { putBoolean("microsoft_app_center_type", enable) }
}

fun 微软统计(): Boolean {
    return getDefaultSharedPreferences().getBoolean("microsoft_app_center_type", false)
}
fun 百度统计(enable: Boolean) {
    edit { putBoolean("baidu_statistics_type", enable) }
}

fun 百度统计(): Boolean {
    return getDefaultSharedPreferences().getBoolean("baidu_statistics_type", false)
}

private fun edit(action: SharedPreferences.Editor.() -> Unit) =
    getDefaultSharedPreferences().edit(action = action)
