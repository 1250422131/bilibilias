package com.imcys.bilibilias.base.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.imcys.bilibilias.R
import com.imcys.bilibilias.common.base.app.BaseApplication
import com.imcys.bilibilias.common.base.app.BaseApplication.Companion.dataKv
import com.imcys.bilibilias.home.ui.model.UserBaseBean
import com.tencent.mmkv.MMKV
import kotlinx.serialization.json.Json

fun Context.getUserDownloadSavePath(): String =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getString(
            "user_download_save_path",
            getExternalFilesDir("download")?.absolutePath
        ) ?: error(getString(R.string.app_user_download_save_path_error))


fun saveUserBaseInfo(userBaseBean: UserBaseBean) {
    dataKv.putString("userBaseBean", Json.encodeToString(userBaseBean))
}

fun getUserBaseInfo(): UserBaseBean? {
    val userBaseBeanStr = dataKv.getString("userBaseBean", "")
    if (userBaseBeanStr.isNullOrEmpty()) return null
    val result = runCatching {
        Json.decodeFromString<UserBaseBean>(userBaseBeanStr)
    }
    return result.getOrNull()
}