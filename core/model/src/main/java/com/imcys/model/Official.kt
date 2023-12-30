package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Official(
    @SerialName("desc")
    val desc: String = "",
    @SerialName("role")
    val role: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("type")
    val type: Int = 0
)