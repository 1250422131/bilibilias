﻿package com.imcys.bilibilias.core.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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