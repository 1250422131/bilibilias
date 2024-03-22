package com.imcys.bilibilias.base.model.user

import kotlinx.serialization.Serializable

@Serializable
data class LikeVideoBean(
    val code: Int,
    val message: String,
    val ttl: Int
)