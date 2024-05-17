package com.imcys.bilibilias.feature.login

sealed interface LoginEvent {
    data object RefreshQrCode : LoginEvent
}
