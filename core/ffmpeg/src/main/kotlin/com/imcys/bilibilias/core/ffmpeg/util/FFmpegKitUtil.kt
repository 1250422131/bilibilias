package com.imcys.bilibilias.core.ffmpeg.util

import android.content.Context
import androidx.core.net.toUri
import com.arthenica.ffmpegkit.FFmpegKitConfig

fun Context.convertSAFProtocol(contentUri: String, read: Boolean): String {
    require(contentUri.startsWith("content://"))
    return if (read) {
        FFmpegKitConfig.getSafParameterForRead(this, contentUri.toUri())
    } else {
        FFmpegKitConfig.getSafParameterForWrite(this, contentUri.toUri())
    }
}
