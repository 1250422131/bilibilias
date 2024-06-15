package com.imcys.bilibilias.core.common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.activity.ComponentActivity
import java.io.File

tailrec fun Context.getActivity(): Activity = when (this) {
    is ComponentActivity -> this
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> error("Permissions should be called in the context of an Activity")
}

// 更新图库
fun updatePhotoMedias(
    context: Context,
    vararg files: File,
    callback: (String, Uri) -> Unit = { _, _ -> }
) {
    MediaScannerConnection.scanFile(
        context,
        files.map { it.path }.toTypedArray(),
        null
    ) { path, uri ->
        callback(path, uri)
    }
}

fun updatePhotoMedias(
    context: Context,
    uri: Uri,
    callback: (String, Uri) -> Unit = { _, _ -> }
) {
    MediaScannerConnection.scanFile(context, arrayOf(uri.path), null) { path, uri ->
        callback(path, uri)
    }
}
