package com.imcys.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Box<T>(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
    @JsonNames("data", "result")
    val data: T
)
