package com.imcys.bilibilias.logic.login

sealed interface LoginState {
    data object GeneratingQrCode : LoginState
    data class QrCodeReady(val qrCodeUrl: String, val qrKey: String) : LoginState
    data class AwaitingConfirmation(val key: String) : LoginState
    data object LoginSuccessful : LoginState
    data class LoginFailed(val message: String) : LoginState

    data object Success : LoginState
    data class Error(val message: String) : LoginState
}