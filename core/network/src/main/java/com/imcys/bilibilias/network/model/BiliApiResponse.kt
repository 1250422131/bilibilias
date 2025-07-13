package com.imcys.bilibilias.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BiliApiResponse<out T>(
    val code: Int,
    val data: T?,
    val result: T?,
    val message: String?,
    val ttl: Int = 0,
    @Transient
    var responseHeader: Set<Map.Entry<String, List<String>>> = setOf()
)