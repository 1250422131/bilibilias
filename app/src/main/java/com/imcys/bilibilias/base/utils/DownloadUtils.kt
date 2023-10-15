package com.imcys.bilibilias.base.utils

import android.content.Context
import android.os.Environment
import com.imcys.bilibilias.common.base.config.SettingsRepository
import java.io.File

private var _bilibilias: File? = null
val DIRECTORY_BILIBILI_AS =
    _bilibilias ?: File(Environment.getExternalStorageDirectory(), "BILIBILIAS").also { _bilibilias = it }


private var _downloadPath: String? = null
val Context.appDownloadPath: String
    get() = _downloadPath ?: File(filesDir, "download").absolutePath.also { _downloadPath = it }

val savePath = SettingsRepository.saveFilePath ?: SettingsRepository.DEFAULT_SAVE_FILE_PATH
val savePath1 = File(
    Environment.getExternalStorageDirectory(),
    SettingsRepository.saveFilePath ?: SettingsRepository.DEFAULT_SAVE_FILE_PATH
)
