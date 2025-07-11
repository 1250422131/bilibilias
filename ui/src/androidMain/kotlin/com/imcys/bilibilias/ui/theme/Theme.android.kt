package com.imcys.bilibilias.ui.theme

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
actual fun supportsDynamicTheming(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S