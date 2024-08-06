package com.imcys.bilibilias.core.model.bangumi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class IconFont(
    @SerialName("name")
    val name: String = "",
    @SerialName("text")
    val text: String = "",
)
