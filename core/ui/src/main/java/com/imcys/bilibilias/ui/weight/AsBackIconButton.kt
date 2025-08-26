package com.imcys.bilibilias.ui.weight

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun AsBackIconButton(onClick: () -> Unit) {
    val haptics = LocalHapticFeedback.current

    ASIconButton(onClick = {
        haptics.performHapticFeedback(HapticFeedbackType.ContextClick)
        onClick.invoke()
    }) {
        Icon(
            Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "返回"
        )
    }
}