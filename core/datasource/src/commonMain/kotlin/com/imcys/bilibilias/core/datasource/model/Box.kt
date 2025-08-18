package com.imcys.bilibilias.core.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
internal data class Box<T>(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("message")
    val message: String = "",
    @JsonNames("data", "result")
    val data: T? = null
)