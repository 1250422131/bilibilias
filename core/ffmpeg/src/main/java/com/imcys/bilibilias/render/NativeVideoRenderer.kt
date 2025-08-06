package com.imcys.bilibilias.render

import android.content.Context
import android.net.Uri
import android.opengl.GLSurfaceView
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
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


class NativeVideoRenderer : GLSurfaceView.Renderer {

    private var rendererPtr = VideoRendererJNI.createRenderer()

    override fun onDrawFrame(p0: GL10?) {
        VideoRendererJNI.onDrawFrame(rendererPtr)
    }

    override fun onSurfaceChanged(
        p0: GL10?,
        p1: Int,
        p2: Int
    ) {
        VideoRendererJNI.onSurfaceChanged(rendererPtr, p1, p2)
    }

    override fun onSurfaceCreated(
        p0: GL10?,
        p1: EGLConfig?
    ) {
        Log.i("TAG", "onSurfaceCreated")
        VideoRendererJNI.onSurfaceCreated(rendererPtr)
    }

    fun startPlayback() {
        VideoRendererJNI.startPlayback(rendererPtr)
    }
    fun pausePlayback() {
        VideoRendererJNI.pausePlayback(rendererPtr)
    }

    fun setVideoFd(fd: Int) {
        VideoRendererJNI.setVideoFd(rendererPtr, fd)
    }



    fun release() {
        VideoRendererJNI.destroyRenderer(rendererPtr)
        rendererPtr = 0
    }
}