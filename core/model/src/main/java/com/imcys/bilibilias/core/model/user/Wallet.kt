package com.imcys.bilibilias.core.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wallet(
    @SerialName("bcoin_balance")
    val bcoinBalance: Int = 0,
    @SerialName("coupon_balance")
    val couponBalance: Int = 0,
    @SerialName("coupon_due_time")
    val couponDueTime: Int = 0,
    @SerialName("mid")
    val mid: Long = 0
)
