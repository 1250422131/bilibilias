package com.imcys.bilibilias.base.utils

import android.content.Context
import androidx.preference.PreferenceManager
fun Context.getUserDownloadSavePath(): String =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getString(
            "user_download_save_path",
            getExternalFilesDir("download")?.absolutePath
        ) ?: error("获取用户下载路径错误")
