package com.imcys.bilibilias.render

import android.graphics.SurfaceTexture
import android.view.Surface
import android.view.TextureView

class VideoSurfaceTextureListener(
    private val onSurfaceReady: (Surface, Int, Int) -> Unit,
    private val onSurfaceChanged: (Int, Int) -> Unit,
    private val onSurfaceDestroyed: () -> Unit
) : TextureView.SurfaceTextureListener {

    private var surface: Surface? = null

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        surface = Surface(surfaceTexture).apply {
            onSurfaceReady(this, width, height)
        }
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        onSurfaceDestroyed()
        return true
    }

    override fun onSurfaceTextureSizeChanged(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        onSurfaceChanged(width, height)
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {

    }

}
