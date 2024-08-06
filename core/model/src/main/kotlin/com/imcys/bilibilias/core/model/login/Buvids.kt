package com.imcys.bilibilias.core.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Buvids(
    @SerialName("b_3")
    val b3: String = "",
    @SerialName("b_4")
    val b4: String = "",
)
