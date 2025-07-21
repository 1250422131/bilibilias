package com.imcys.bilibilias.ui.component

import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@Composable
fun SwipeBox(
    actionWidth: Dp,
    modifier: Modifier = Modifier,
    control: SwipeBoxControl = rememberSwipeBoxControl(),
    startAction: List<@Composable BoxScope.() -> Unit> = listOf(),
    startFillAction: (@Composable BoxScope.() -> Unit)? = null,
    endAction: List<@Composable BoxScope.() -> Unit> = listOf(),
    endFillAction: (@Composable BoxScope.() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val actionWidthPx = with(density) {
        actionWidth.toPx()
    }
    val startWidth = actionWidthPx * startAction.size
    val startActionSize =
        if (startFillAction == null) startAction.size else startAction.size + 1 // startAction + startFillAction
    val endWidth = actionWidthPx * endAction.size
    val endActionSize =
        if (endFillAction == null) endAction.size else endAction.size + 1 // endAction + endFillAction
    var contentWidth by remember { mutableFloatStateOf(0f) }
    var contentHeight by remember { mutableFloatStateOf(0f) }
    val state = remember(startWidth, endWidth, contentWidth) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at (if (startFillAction != null) actionWidthPx else 0f) + startWidth
                DragAnchors.StartFill at (if (startFillAction != null) contentWidth else 0f) + startWidth
                DragAnchors.Center at 0f
                DragAnchors.End at (if (endFillAction != null) -actionWidthPx else 0f) - endWidth
                DragAnchors.EndFill at (if (endFillAction != null) -contentWidth else 0f) - endWidth
            },
        )
    }
    LaunchedEffect(control, state) {
        with(control) {
            handleControlEvents(
                onStart = {
                    scope.launch {
                        state.animateTo(DragAnchors.Start)
                    }
                },
                onStartFill = {
                    scope.launch {
                        state.animateTo(DragAnchors.StartFill)
                    }
                },
                onCenter = {
                    scope.launch {
                        state.animateTo(DragAnchors.Center)
                    }
                },
                onEnd = {
                    scope.launch {
                        state.animateTo(DragAnchors.End)
                    }
                },
                onEndFill = {
                    scope.launch {
                        state.animateTo(DragAnchors.EndFill)
                    }
                }
            )
        }
    }
    Box(
        modifier = modifier
            .anchoredDraggable(
                state = state,
                orientation = Orientation.Horizontal,
            )
            .clipToBounds()
    ) {
        startAction.forEachIndexed { index, action ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(actionWidth)
                    .height(with(density) {
                        contentHeight.toDp()
                    })
                    .offset {
                        IntOffset(
                            x = if (state.offset <= actionWidthPx * startActionSize) {
                                (-actionWidthPx + state.offset / startActionSize * (startActionSize - index)).roundToInt()
                            } else {
                                (-actionWidthPx * (index + 1) + state.offset).roundToInt()
                            },
                            y = 0,
                        )
                    }
            ) {
                action()
            }
        }
        startFillAction?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .height(with(density) {
                        contentHeight.toDp()
                    })
                    .offset {
                        IntOffset(
                            x = if (state.offset <= actionWidthPx * startActionSize) {
                                (-contentWidth + state.offset / startActionSize).roundToInt()
                            } else {
                                (-contentWidth - startWidth + state.offset).roundToInt()
                            },
                            y = 0,
                        )
                    }
            ) {
                it()
            }
        }
        endAction.forEachIndexed { index, action ->
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(actionWidth)
                    .height(with(density) {
                        contentHeight.toDp()
                    })
                    .offset {
                        IntOffset(
                            x = if (state.offset >= -(actionWidthPx * endActionSize)) {
                                (actionWidthPx + state.offset / endActionSize * (endActionSize - index)).roundToInt()
                            } else {
                                (actionWidthPx * (index + 1) + state.offset).roundToInt()
                            },
                            y = 0,
                        )
                    }
            ) {
                action()
            }
        }
        endFillAction?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .height(with(density) {
                        contentHeight.toDp()
                    })
                    .offset {
                        IntOffset(
                            x = if (state.offset >= -(actionWidthPx * endActionSize)) {
                                (contentWidth + state.offset / endActionSize).roundToInt()
                            } else {
                                (contentWidth + endWidth + state.offset).roundToInt()
                            },
                            y = 0,
                        )
                    }
            ) {
                it()
            }
        }
        Box(
            modifier = Modifier
                .onSizeChanged {
                    contentWidth = it.width.toFloat()
                    contentHeight = it.height.toFloat()
                }
                .offset {
                    IntOffset(
                        x = state.offset.roundToInt(),
                        y = 0,
                    )
                }
        ) {
            content()
        }
    }
}

@Stable
class SwipeBoxControl(
    private val scope: CoroutineScope
) {
    private sealed interface ControlEvent {
        data object Start : ControlEvent
        data object StartFill : ControlEvent
        data object Center : ControlEvent
        data object End : ControlEvent
        data object EndFill : ControlEvent
    }

    private val controlEvents: MutableSharedFlow<ControlEvent> = MutableSharedFlow()

    @OptIn(FlowPreview::class)
    internal suspend fun handleControlEvents(
        onStart: () -> Unit = {},
        onStartFill: () -> Unit = {},
        onCenter: () -> Unit = {},
        onEnd: () -> Unit = {},
        onEndFill: () -> Unit = {},
    ) = withContext(Dispatchers.Main) {
        controlEvents.debounce(350).collect { event ->
            when (event) {
                ControlEvent.Start -> onStart()
                ControlEvent.StartFill -> onStartFill()
                ControlEvent.Center -> onCenter()
                ControlEvent.End -> onEnd()
                ControlEvent.EndFill -> onEndFill()
            }
        }
    }

    fun start() {
        scope.launch { controlEvents.emit(ControlEvent.Start) }
    }

    fun startFill() {
        scope.launch { controlEvents.emit(ControlEvent.Start) }
    }

    fun center() {
        scope.launch { controlEvents.emit(ControlEvent.Center) }
    }

    fun end() {
        scope.launch { controlEvents.emit(ControlEvent.End) }
    }

    fun endFill() {
        scope.launch { controlEvents.emit(ControlEvent.EndFill) }
    }
}

@Composable
fun rememberSwipeBoxControl(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): SwipeBoxControl = remember(coroutineScope) { SwipeBoxControl(coroutineScope) }

enum class DragAnchors {
    Start,
    StartFill,
    Center,
    End,
    EndFill,
}