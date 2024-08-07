package com.imcys.bilibilias.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import dev.DevUtils
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

tailrec fun Context.getActivity(): Activity = when (this) {
    is ComponentActivity -> this
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> error("Permissions should be called in the context of an Activity")
}

suspend fun scanFile(file: File, mimeType: String): Uri? = suspendCancellableCoroutine { continuation ->
    MediaScannerConnection.scanFile(
        DevUtils.getContext(),
        arrayOf(file.toString()),
        arrayOf(mimeType),
    ) { _, scannedUri ->
        if (scannedUri == null) {
            continuation.cancel(Exception("File $file could not be scanned"))
        } else {
            continuation.resume(scannedUri)
        }
    }
}

suspend fun scanUri(uri: Uri, mimeType: String): Uri? {
    val context = DevUtils.getContext()
    val cursor = context.contentResolver.query(
        uri,
        arrayOf(MediaStore.Files.FileColumns.DATA),
        null,
        null,
        null,
    ) ?: throw Exception("Uri $uri could not be found")

    val path = cursor.use {
        if (!cursor.moveToFirst()) {
            throw Exception("Uri $uri could not be found")
        }

        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
    }

    return suspendCancellableCoroutine { continuation ->
        MediaScannerConnection.scanFile(
            context,
            arrayOf(path),
            arrayOf(mimeType),
        ) { _, scannedUri ->
            if (scannedUri == null) {
                continuation.cancel(Exception("File $path could not be scanned"))
            } else {
                continuation.resume(scannedUri)
            }
        }
    }
}
