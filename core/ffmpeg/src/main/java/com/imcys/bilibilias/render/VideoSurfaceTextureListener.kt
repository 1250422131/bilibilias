package com.imcys.bilibilias.render

import android.graphics.SurfaceTexture
import android.view.TextureView

class VideoSurfaceTextureListener : TextureView.SurfaceTextureListener {
    override fun onSurfaceTextureAvailable(
        p0: SurfaceTexture,
        p1: Int,
        p2: Int
    ) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureSizeChanged(
        p0: SurfaceTexture,
        p1: Int,
        p2: Int
    ) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

    }

}