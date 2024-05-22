package com.imcys.bilibilias.feature.login

import androidx.compose.ui.graphics.painter.Painter

data class LoginModel(
    val isSuccess: Boolean,
    val message: String,
    val qrCodePainter: Painter,
    val requiredGoTO: Boolean
)
