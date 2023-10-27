package com.imcys.bilibilias.ui.player.compoent.utils

import android.app.Activity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.Window

/**
 * Bring the activity to the full screen.
 */
internal fun Activity.setFullScreen(fullscreen: Boolean) {
    window.setFullScreen(fullscreen)
}

/**
 * Bring the window to full screen. (Remove the status bar and navigation bar.)
 */
@Suppress("Deprecation")
internal fun Window.setFullScreen(fullscreen: Boolean) {
    if (fullscreen) {
        decorView.systemUiVisibility = (
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    } else {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}