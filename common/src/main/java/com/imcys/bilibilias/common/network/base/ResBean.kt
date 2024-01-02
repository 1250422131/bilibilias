package com.imcys.bilibilias.common.network.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResBean<T>(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("data")
    val data: T,
    @SerialName("message")
    val message: String = "",
    @SerialName("ttl")
    val ttl: Int = 0
):java.io.Serializable
