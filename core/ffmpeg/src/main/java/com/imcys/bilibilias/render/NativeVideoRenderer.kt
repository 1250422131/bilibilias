package com.imcys.bilibilias.render

import android.content.Context
import android.util.Log
import androidx.core.net.toUri

fun getFileDescriptorFromContentUri(context: Context, uriString: String): Int? {
    return try {
        val uri = uriString.toUri()
        val pfd = context.contentResolver.openFileDescriptor(uri, "r")
        pfd?.let {
            val fd = it.detachFd()
            Log.d("FileDescriptor", "Got FD: $fd for URI: $uriString")
            fd
        }
    } catch (e: Exception) {
        Log.e("FileDescriptor", "Failed to get FD for URI: $uriString", e)
        null
    }
}

