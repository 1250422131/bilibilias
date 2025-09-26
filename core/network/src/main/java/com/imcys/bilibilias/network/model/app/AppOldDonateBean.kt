package com.imcys.bilibilias.network.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AppOldDonateBean(
    val code: Int = 0,
    @SerialName("Alipay")
    val alipay: String = "",
    @SerialName("WeChat")
    val weChat: String = "",
    @SerialName("Surplus")
    val surplus: String = "",
    @SerialName("Total")
    val total: String = "",
)