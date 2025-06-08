package com.imcys.bilibilias.base.model.webi

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
@Serializable
data class WebSpiBean(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val `data`: Data,
    @SerialName("message")
    val message: String
) {
    @Serializable
    data class Data(
        @SerialName("b_3")
        val b3: String,
        @SerialName("b_4")
        val b4: String
    )
}


