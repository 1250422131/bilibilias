package com.sockmagic.login

sealed interface LoginEvent {
    data object RefreshQrCode : LoginEvent
}
