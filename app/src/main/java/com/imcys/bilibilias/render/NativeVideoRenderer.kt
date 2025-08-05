package com.imcys.bilibilias.render

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

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
        VideoRendererJNI.onSurfaceCreated(rendererPtr)
    }

    fun release() {
        VideoRendererJNI.destroyRenderer(rendererPtr)
        rendererPtr = 0
    }
}