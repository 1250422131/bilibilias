package com.imcys.bilibilias.common.base.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BottomSheetDialog(
    visible: Boolean,
    modifier: Modifier = Modifier,
    cancelable: Boolean = true,
    canceledOnTouchOutside: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler(enabled = visible, onBack = {
        if (cancelable) {
            onDismissRequest()
        }
    })
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(durationMillis = 400, easing = LinearEasing)),
            exit = fadeOut(animationSpec = tween(durationMillis = 400, easing = LinearEasing))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(color = 0x4D000000))
                    .clickableNoRipple {
                        if (canceledOnTouchOutside) {
                            onDismissRequest()
                        }
                    }
            )
        }
        InnerDialog(
            visible = visible,
            cancelable = cancelable,
            onDismissRequest = onDismissRequest,
            content = content
        )
    }
}

@Composable
private fun BoxScope.InnerDialog(
    visible: Boolean,
    cancelable: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    var offsetY by remember {
        mutableFloatStateOf(value = 0f)
    }
    val offsetYAnimate by animateFloatAsState(targetValue = offsetY, label = "")
    var bottomSheetHeight by remember { mutableFloatStateOf(value = 0f) }
    AnimatedVisibility(
        modifier = Modifier
            .align(alignment = Alignment.BottomCenter)
            .clickableNoRipple {}
            .onGloballyPositioned {
                bottomSheetHeight = it.size.height.toFloat()
            }
            .offset(offset = {
                IntOffset(0, offsetYAnimate.roundToInt())
            })
            .draggable(
                state = rememberDraggableState(
                    onDelta = {
                        offsetY = (offsetY + it.toInt()).coerceAtLeast(0f)
                    }
                ),
                orientation = Orientation.Vertical,
                onDragStarted = {},
                onDragStopped = {
                    if (cancelable && offsetY > bottomSheetHeight / 2) {
                        onDismissRequest()
                    } else {
                        offsetY = 0f
                    }
                }
            ),
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
            initialOffsetY = {
                2 * it
            }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
            targetOffsetY = {
                it
            }
        )
    ) {
        DisposableEffect(key1 = null) {
            onDispose {
                offsetY = 0f
            }
        }
        Box(
            modifier = Modifier.clip(
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
        ) {
            content()
        }
    }
}

private fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier =
    composed {
        clickable(
            onClick = onClick,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
