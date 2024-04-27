package com.imcys.bilibilias.compose.login

data class LoginUiState(
    val qrCodeUrl: String = "",
    val message: String = "",
    val success: Boolean = false
)
