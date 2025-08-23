package com.imcys.bilibilias.core.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PollResponse(
    @SerialName("code")
    private val code: Int,
    @SerialName("message")
    val message: String,
    @SerialName("refresh_token")
    val refreshToken: String,
) {
    @Transient
    val status = OauthCode.parse(code)
}

@JvmInline
value class OauthCode(val code: Int) {
    companion object {
        val Success = OauthCode(0)
        val WaitingScanned = OauthCode(86101)
        val WaitingConfirmation = OauthCode(86090)
        val Expired = OauthCode(86038)
        fun parse(code: Int): OauthCode? {
            return when (code) {
                0 -> Success
                86101 -> WaitingScanned
                86090 -> WaitingConfirmation
                86038 -> Expired
                else -> null
            }
        }
    }
}