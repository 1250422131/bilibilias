package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class AppOldApplyRoamBean(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("msg")
    val msg: String,
    @SerialName("reason")
    val reason: String? = null,
    @SerialName("status")
    val status : Int = -1,
    @SerialName("reject_reason")
    val rejectReason : String? = null,
)
