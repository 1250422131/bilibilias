package com.imcys.bilibilias.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class WebSpiInfo(
    @SerialName("b_3")
    val b3: String,
    @SerialName("b_4")
    val b4: String
)