package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.Serializable

/**
 * 旧的捐款数据
 */
@Serializable
data class OldDonateBean(
    val code: Int = 0,
    val alipay: String = "",
    val weChat: String = "",
    val surplus: String = "",
    val total: String = "",
)
