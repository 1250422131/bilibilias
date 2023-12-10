package com.imcys.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.LongAsStringSerializer

internal typealias LongAsString = @Serializable(LongAsStringSerializer::class) Long

@Serializable
data class Box<T>(val code: Int, val message: String, val data: T)
