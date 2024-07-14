package com.imcys.bilibilias.ui

import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children

/**
 * A [DeviceConfigurationOverride] that allows overriding the [windowInsets] available
 * to the content under test.
 */
@Suppress("ktlint:standard:function-naming")
fun DeviceConfigurationOverride.Companion.WindowInsets(
    windowInsets: WindowInsetsCompat,
): DeviceConfigurationOverride = DeviceConfigurationOverride { contentUnderTest ->
    val currentContentUnderTest by rememberUpdatedState(contentUnderTest)
    val currentWindowInsets by rememberUpdatedState(windowInsets)
    AndroidView(
        factory = { context ->
            object : FrameLayout(context) {
                override fun dispatchApplyWindowInsets(insets: WindowInsets): WindowInsets {
                    children.forEach {
                        it.dispatchApplyWindowInsets(currentWindowInsets.toWindowInsets())
                    }
                    return WindowInsetsCompat.CONSUMED.toWindowInsets()!!
                }

                /**
                 * Deprecated, but intercept the `requestApplyInsets` call via the deprecated
                 * method.
                 */
                @Deprecated("Deprecated in Java")
                override fun requestFitSystemWindows() {
                    dispatchApplyWindowInsets(currentWindowInsets.toWindowInsets()!!)
                }
            }.apply {
                addView(
                    ComposeView(context).apply {
                        setContent {
                            currentContentUnderTest()
                        }
                    },
                )
            }
        },
    )
}
