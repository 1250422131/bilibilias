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

class NativePlayer {
    private var playerPtr: Ptr = 0

    fun init(surface: Surface, videoFileFd: Int): Boolean {
        if (playerPtr == 0L) {
            playerPtr = PlayerJNI.createPlayer()
        }
        if (playerPtr == 0L) {
            return false
        }
        return try {
            PlayerJNI.initPlayer(playerPtr, surface, videoFileFd)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun setViewport(width: Int, height: Int) {
        if (playerPtr != 0L) {
            PlayerJNI.setPlayerViewports(playerPtr, width, height)
        }
    }

    fun play() {
        if (playerPtr != 0L) {
            PlayerJNI.play(playerPtr)
        }
    }

    fun pause() {
        if (playerPtr != 0L) {
            PlayerJNI.pause(playerPtr)
        }
    }

    fun stop() {
        if (playerPtr != 0L) {
            PlayerJNI.stop(playerPtr)
        }
    }

    fun release() {
        if (playerPtr != 0L) {
            PlayerJNI.releasePlayer(playerPtr)
            playerPtr = 0
        }
    }
}
