package com.imcys.bilibilias.render

import android.content.Context
import android.util.Log
import android.view.Surface
import androidx.core.net.toUri
import com.imcys.bilibilias.Ptr

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

class NativeVideoRenderer {
    private var inner: Ptr = 0

    fun init(surface: Surface, videoFileFd: Int): Boolean {
        if (inner == 0L) {
            inner = RenderUtilJNI.createRenderer()
        }
        if (inner == 0L) {
            return false;
        }
        return RenderUtilJNI.initRenderer(inner, surface, videoFileFd)
    }

    fun setViewport(width: Int, height: Int) {
        if (inner != 0L) {
            RenderUtilJNI.setRendererViewports(inner, width, height)
        }
    }

    fun release() {
        if (inner != 0L) {
            RenderUtilJNI.releaseRenderer(inner)
            inner = 0
        }
    }
}
