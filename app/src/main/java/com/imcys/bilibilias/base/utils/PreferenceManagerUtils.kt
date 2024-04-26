package com.imcys.bilibilias.base.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.R

fun Context.getUserDownloadSavePath(): String =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getString(
            "user_download_save_path",
            getExternalFilesDir("download")?.absolutePath
        ) ?: error(getString(R.string.app_user_download_save_path_error))
