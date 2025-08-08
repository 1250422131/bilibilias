package com.imcys.bilibilias.render

import android.view.Surface
import com.imcys.bilibilias.Ptr
import com.imcys.bilibilias.ffmpeg.FFmpegManger

object RenderUtilJNI {

    init {
        FFmpegManger
    }

    @JvmStatic
    external fun setSurface(surface: Surface): Ptr

    @JvmStatic
    external fun createRenderer(): Ptr

    @JvmStatic
    external fun initRenderer(renderer: Ptr, surface: Surface, fd: Int): Boolean

    @JvmStatic
    external fun setRendererViewports(renderer: Ptr, width: Int, height: Int)

    @JvmStatic
    external fun releaseRenderer(renderer: Ptr)

    @JvmStatic
    external fun testPlay(renderer: Ptr)
}
