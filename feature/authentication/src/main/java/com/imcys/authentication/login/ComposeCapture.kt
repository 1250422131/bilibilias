package com.imcys.authentication.login

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

data class CaptureState(
    val capture: Boolean = false
)

fun MutableState<CaptureState>.capture() {
    this.value = this.value.copy(capture = true)
}

private fun MutableState<CaptureState>.captureComplete() {
    this.value = this.value.copy(capture = false)
}

@Composable
fun rememberCaptureController(): MutableState<CaptureState> {
    return remember {
        mutableStateOf(CaptureState(capture = false))
    }
}

@Composable
fun ComposeCapture(
    captureController: MutableState<CaptureState> = rememberCaptureController(),
    onSaveBitmap: (Bitmap?) -> Unit,
    content: @Composable () -> Unit
) {
    var bounds = remember {
        mutableStateOf<Rect?>(null)
    }
    // 依据状态值 选择是否使用AndroidView进行展示获取截图
    if (captureController.value.capture) {
        CaptureView(
            captureController = captureController,
            onSaveBitmap = onSaveBitmap,
            bounds = bounds,
            content = content
        )
    } else {
        Surface(
            modifier = Modifier.onGloballyPositioned {
                bounds.value = it.boundsInRoot()
            },
            content = content
        )
    }
}

@Composable
private fun CaptureView(
    captureController: MutableState<CaptureState>,
    bounds: MutableState<Rect?>,
    onSaveBitmap: ((Bitmap?) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    AndroidView(
        factory = {
            FrameLayout(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    (bounds.value!!.right - bounds.value!!.left).toInt(),
                    (bounds.value!!.bottom - bounds.value!!.top).toInt()
                )
                val composeView = ComposeView(it).apply {
                    setContent {
                        content()
                    }
                }
                drawListener(composeView, this, captureController, onSaveBitmap)
                addView(
                    composeView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    )
}

private fun drawListener(
    composeView: View,
    viewGroup: ViewGroup,
    captureController: MutableState<CaptureState>,
    onSaveBitmap: ((Bitmap?) -> Unit)? = null,
) {
    val drawListener = object : ViewTreeObserver.OnDrawListener {
        var remove = false
        override fun onDraw() {
            if (composeView.width > 0) {
                if (!remove) {
                    // View 绘制第一帧 开始截图并移除 监听，随后切换截图状态 回到Compose组件
                    remove = true
                    composeView.post {
                        val bitmap = getViewGroupBitmap(viewGroup)
                        // 切换状态 回到Compose
                        captureController.captureComplete()
                        onSaveBitmap?.invoke(bitmap)
                        composeView.viewTreeObserver.removeOnDrawListener(this)
                    }
                }
            }
        }
    }
    composeView.viewTreeObserver.addOnDrawListener(drawListener)
}

private fun getViewGroupBitmap(viewGroup: ViewGroup): Bitmap {
    val bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    viewGroup.draw(canvas)
    return bitmap
}
