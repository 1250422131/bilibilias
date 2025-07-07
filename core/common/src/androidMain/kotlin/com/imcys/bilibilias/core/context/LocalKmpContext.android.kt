package com.imcys.bilibilias.core.context

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

actual val LocalKmpContext: ProvidableCompositionLocal<KmpContext> =
    staticCompositionLocalOf { error("CompositionLocal LocalApplicationContext not present") }