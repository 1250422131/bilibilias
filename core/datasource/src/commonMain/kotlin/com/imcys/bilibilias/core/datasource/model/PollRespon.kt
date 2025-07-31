package com.imcys.bilibilias.core.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class PollResponse(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("message")
    val message: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("timestamp")
    val timestamp: Long = 0,
    @SerialName("url")
    val url: String = ""
) {
    @Deprecated("use OauthCode")
    val isSuccess: Boolean = code == 0

    @Transient
    val status: OauthCode = OauthCode.parse(code)
}

@JvmInline
value class OauthCode(val code: Int) {
    companion object {
        val Success = OauthCode(0)
        val WaitingScanned = OauthCode(86101)
        val waitingConfirmation = OauthCode(86090)
        val Expired = OauthCode(86038)
        fun parse(code: Int): OauthCode {
            return when (code) {
                0 -> Success
                86101 -> WaitingScanned
                86090 -> waitingConfirmation
                86038 -> Expired
                else -> throw IllegalArgumentException("Unknown oauth code: $code")
            }
        }
    }
}