package com.imcys.bilibilias.base.utils

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

fun Context.getDefaultSharedPreferences() =
    PreferenceManager.getDefaultSharedPreferences(this)

fun Context.getUserDownloadSavePath(): String =
    getDefaultSharedPreferences().getString(
        "user_download_save_path",
        getExternalFilesDir("download")?.absolutePath
    ) ?: error("获取用户下载路径错误")

fun Context.getAppNotices(): String =
    getDefaultSharedPreferences()
        .getString("AppNotice", "") ?: error("获取 APP 更新日志错误")

fun Context.setAppNotices(notice: String) =
    getDefaultSharedPreferences().edit {
        putString("AppNotice", notice)
    }

fun Context.getUserSetDownloadFileName() = getDefaultSharedPreferences().getString(
    "user_download_file_name_editText",
    "{BV}/{FILE_TYPE}/{P_TITLE}_{CID}.{FILE_TYPE}",
) ?: error("获取用户设置的文件名设置错误")
