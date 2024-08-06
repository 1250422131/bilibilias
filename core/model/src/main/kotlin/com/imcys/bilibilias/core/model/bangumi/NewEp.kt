package com.imcys.bilibilias.core.model.bangumi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class NewEp(
    @SerialName("desc")
    val desc: String = "",
    @SerialName("id")
    val id: Int = 0,
    @SerialName("is_new")
    val isNew: Int = 0,
    @SerialName("title")
    val title: String = "",
)
