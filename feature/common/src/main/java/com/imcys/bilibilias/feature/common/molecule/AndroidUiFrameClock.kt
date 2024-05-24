package com.imcys.bilibilias.feature.common.molecule

import android.view.Choreographer
import androidx.compose.runtime.MonotonicFrameClock
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.coroutineContext

public class AndroidUiFrameClock(
    private val choreographer: Choreographer,
) : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (Long) -> R,
    ): R {
        val uiDispatcher = coroutineContext[ContinuationInterceptor] as? AndroidUiDispatcher
        return suspendCancellableCoroutine { co ->
            // Important: this callback won't throw, and AndroidUiDispatcher counts on it.
            val callback = Choreographer.FrameCallback { frameTimeNanos ->
                co.resumeWith(runCatching { onFrame(frameTimeNanos) })
            }

            // If we're on an AndroidUiDispatcher then we post callback to happen *after*
            // the greedy trampoline dispatch is complete.
            // This means that onFrame will run on the current choreographer frame if one is
            // already in progress, but withFrameNanos will *not* resume until the frame
            // is complete. This prevents multiple calls to withFrameNanos immediately dispatching
            // on the same frame.

            if (uiDispatcher != null && uiDispatcher.choreographer == choreographer) {
                uiDispatcher.postFrameCallback(callback)
                co.invokeOnCancellation { uiDispatcher.removeFrameCallback(callback) }
            } else {
                choreographer.postFrameCallback(callback)
                co.invokeOnCancellation { choreographer.removeFrameCallback(callback) }
            }
        }
    }
}
