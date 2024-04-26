package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 旧的捐款数据
 */

@Serializable
data class OldDonateBean(
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
