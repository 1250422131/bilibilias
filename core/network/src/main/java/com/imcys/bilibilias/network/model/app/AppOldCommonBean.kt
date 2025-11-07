package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppOldCommonBean(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("msg")
    val msg: String,
)