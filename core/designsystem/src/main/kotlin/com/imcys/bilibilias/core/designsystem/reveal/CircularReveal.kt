package com.imcys.bilibilias.core.designsystem.reveal

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import kotlin.math.sqrt

/**
 * A modifier that clips the composable content using a circular reveal animation. The circle will
 * expand or shrink whenever [isVisible] changes.
 *
 * For more control over the transition, consider using this method's variant which allows passing
 * a [State] object to control the progress of the reveal animation.
 *
 * By default, the circle is centered in the content. However, custom positions can be specified using
 * [revealFrom]. The specified offsets should range from 0 (left/top) to 1 (right/bottom).
 *
 * @param isVisible Determines whether content is visible or not. If true, circle expands; if false, it shrinks.
 * @param revealFrom Custom position from which to start the circular reveal. Default is center of content.
 * @param durationMillis Duration of animation in milliseconds. Default is 250ms.
 * @param easing Easing function used for animation. Default is EaseInOutSine.
 * @param positioned Size state of component being revealed. Default size is (0, 0).
 */
fun Modifier.circularReveal(
    isVisible: Boolean,
    revealFrom: Offset = Offset(0.5f, 0.5f),
    durationMillis: Int = 250,
    easing: Easing = EaseInOutSine,
    positioned: (IntSize) -> Unit = {},
    finishedListener: () -> Unit = {}
): Modifier {
    return composed(
        inspectorInfo = debugInspectorInfo {
            name = "circularReveal"
            properties["visible"] = isVisible
            properties["revealFrom"] = revealFrom
            properties["durationMillis"] = durationMillis
        }
    ) {
        val position = remember { mutableStateOf(IntSize(0, 0)) }
        onGloballyPositioned {
            position.value = it.size
            positioned(it.size)
        }
        val animationProgress: State<Float> = animateFloatAsState(
            targetValue = if (isVisible) 1f else 0f,
            animationSpec = tween(durationMillis = durationMillis, easing = easing),
            label = "circularReveal",
            finishedListener = { finishedListener() }
        )
        circularReveal(animationProgress, revealFrom / position.value.toSize())
    }
}

/**
 * A modifier that applies a circular reveal animation to the composable content using a transition progress state.
 * The radius of the circle used for clipping will be calculated based on the transition progress.
 *
 * @param transitionProgress The state of progress for the transition. This determines the radius of the circle used for clipping.
 * @param revealFrom The position from which to start the circular reveal. Default is center of content.
 */
private fun Modifier.circularReveal(
    transitionProgress: State<Float>,
    revealFrom: Offset = Offset(0.5f, 0.5f)
): Modifier = drawWithCache {
    val path = Path()

    val center = revealFrom * size
    val radius = calculateRadius(revealFrom, size)

    path.addOval(Rect(center, radius * transitionProgress.value))

    onDrawWithContent {
        clipPath(path) { this@onDrawWithContent.drawContent() }
    }
}

private operator fun Offset.times(size: Size): Offset = Offset(x * size.width, y * size.height)

private operator fun Offset.div(size: Size): Offset {
    val dx = if (size.width == 0f) x else x / size.width
    val dy = if (size.height == 0f) y else y / size.height
    return Offset(dx, dy)
}

private fun calculateRadius(normalizedOrigin: Offset, size: Size) = with(normalizedOrigin) {
    val x = (if (x > 0.5f) x else 1 - x) * size.width
    val y = (if (y > 0.5f) y else 1 - y) * size.height
    sqrt(x * x + y * y)
}
