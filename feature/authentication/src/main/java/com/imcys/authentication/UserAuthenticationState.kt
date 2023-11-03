package com.imcys.authentication

sealed interface UserAuthenticationState {
    /**
     * 身份验证中
     */
    data object AuthFailed : UserAuthenticationState
    data class AuthSuccess(val imageUrl: String, val qrcodeKey: String) : UserAuthenticationState
    data class StartPoll(val qrcodeKey: String) : UserAuthenticationState

}