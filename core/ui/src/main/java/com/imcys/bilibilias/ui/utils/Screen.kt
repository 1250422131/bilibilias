package com.imcys.bilibilias.ui.utils

import android.app.Activity
import android.content.Context
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberWidthSizeClass(context: Context = LocalContext.current): WindowWidthSizeClass {
    val activity = context as Activity?
    if (activity == null) { return remember { WindowWidthSizeClass.Compact } }
    val windowSizeClass = calculateWindowSizeClass(activity)
    return remember(windowSizeClass) { windowSizeClass.widthSizeClass }
}