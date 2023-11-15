package com.imcys.network.download

import android.content.Context
import android.os.Environment
import java.io.File

private var _bilibilias: File? = null
val DIRECTORY_BILIBILI_AS =
    _bilibilias ?: File(Environment.getExternalStorageDirectory(), "BILIBILIAS").also { _bilibilias = it }

private var _downloadPath: String? = null
val Context.appDownloadPath: String
    get() = _downloadPath ?: File(filesDir, "download").absolutePath.also { _downloadPath = it }
