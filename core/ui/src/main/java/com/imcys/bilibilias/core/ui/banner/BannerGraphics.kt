package com.imcys.bilibilias.core.ui.banner

import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.util.lerp

fun GraphicsLayerScope.scaleInGraphics(offset: Float, start: Float = 0.85f, stop: Float = 1f) {
    lerp(
        start = start,
        stop = stop,
        fraction = 1f - offset.coerceIn(0f, 1f)
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }
}

fun GraphicsLayerScope.alphaInGraphics(offset: Float, start: Float = 0.5f, stop: Float = 1f) {
    alpha = lerp(
        start = start,
        stop = stop,
        fraction = 1f - offset.coerceIn(0f, 1f)
    )
}
