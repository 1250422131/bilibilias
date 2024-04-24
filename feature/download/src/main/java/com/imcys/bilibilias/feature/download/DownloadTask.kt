package com.imcys.bilibilias.feature.download

import android.net.Uri
import com.imcys.bilibilias.core.model.download.FileType
import com.imcys.bilibilias.core.model.video.ViewInfo

data class DownloadTask(
    val id: Int,
    val viewInfo: ViewInfo,
    val subTitle: String,
    val fileType: FileType,
    val uri: Uri,
    val progress: Float,
    val state: String,
)
