package com.imcys.player.appwidget

import androidx.compose.runtime.Stable
import androidx.media3.common.Player

/**
 * VideoPlayer repeat mode.
 */
@Stable
@Suppress("UNUSED_PARAMETER")
enum class RepeatMode(rawValue: String) {
    /**
     * No repeat.
     */
    NONE("none"),

    /**
     * Repeat current media only.
     */
    ONE("one"),

    /**
     * Repeat all track.
     */
    ALL("all"),
}

/**
 * Convert [RepeatMode] to exoplayer repeat mode.
 *
 * @return [Player.REPEAT_MODE_ALL] or [Player.REPEAT_MODE_OFF] or [Player.REPEAT_MODE_ONE] or
 */
internal fun RepeatMode.toExoPlayerRepeatMode(): Int =
    when (this) {
        RepeatMode.NONE -> Player.REPEAT_MODE_OFF
        RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        RepeatMode.ONE -> Player.REPEAT_MODE_ONE
    }

/**
 * Convert exoplayer repeat mode to [RepeatMode].
 *
 * @return [RepeatMode.ALL] or [RepeatMode.NONE] or [RepeatMode.ONE]
 */
fun Int.toRepeatMode(): RepeatMode =
    if (this in 0 until 3) {
        when (this) {
            0 -> RepeatMode.NONE
            1 -> RepeatMode.ONE
            2 -> RepeatMode.ALL
            else -> throw IllegalStateException("This is not ExoPlayer repeat mode.")
        }
    } else {
        throw IllegalStateException("This is not ExoPlayer repeat mode.")
    }