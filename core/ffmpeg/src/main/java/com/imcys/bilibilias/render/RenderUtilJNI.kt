package com.imcys.bilibilias.render

import android.view.Surface
import com.imcys.bilibilias.Ptr
import com.imcys.bilibilias.ffmpeg.FFmpegManger

object PlayerJNI {

    init {
        // Ensures the native library is loaded
        FFmpegManger
    }

    /**
     * Creates a native PlayerController instance.
     * @return A pointer to the native instance.
     */
    @JvmStatic
    external fun createPlayer(): Ptr

    /**
     * Initializes the native PlayerController with a surface and file descriptor.
     * @param playerPtr Pointer to the native PlayerController instance.
     * @param surface The rendering surface.
     * @param fd The file descriptor of the media file to play.
     */
    @JvmStatic
    external fun initPlayer(playerPtr: Ptr, surface: Surface, fd: Int)

    /**
     * Sets the viewport dimensions for the renderer.
     * @param playerPtr Pointer to the native PlayerController instance.
     * @param width The width of the viewport.
     * @param height The height of the viewport.
     */
    @JvmStatic
    external fun setPlayerViewports(playerPtr: Ptr, width: Int, height: Int)

    /**
     * Starts or resumes playback.
     * @param playerPtr Pointer to the native PlayerController instance.
     */
    @JvmStatic
    external fun play(playerPtr: Ptr)

    /**
     * Pauses playback.
     * @param playerPtr Pointer to the native PlayerController instance.
     */
    @JvmStatic
    external fun pause(playerPtr: Ptr)

    /**
     * Stops playback and releases resources associated with the current media.
     * @param playerPtr Pointer to the native PlayerController instance.
     */
    @JvmStatic
    external fun stop(playerPtr: Ptr)


    /**
     * Releases the native PlayerController instance and all its resources.
     * @param playerPtr Pointer to the native PlayerController instance.
     */
    @JvmStatic
    external fun releasePlayer(playerPtr: Ptr)

}
