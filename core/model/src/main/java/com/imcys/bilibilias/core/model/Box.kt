package com.imcys.bilibilias.core.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Box<T>(
    val code: Int,
    val message: String,
    @JsonNames("data", "result")
    val data: T? = null
)
