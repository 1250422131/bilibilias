package com.imcys.bilibilias.ui.utils

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType


fun HapticFeedback.switchHapticFeedback(it: Boolean) {
    if (it) {
        performHapticFeedback(HapticFeedbackType.ToggleOn)
    } else {
        performHapticFeedback(HapticFeedbackType.ToggleOff)
    }
}