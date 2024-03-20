package com.imcys.bilibilias.core.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeVideoBean(
    val code: Int,
    val message: String,
    val ttl: Int
)