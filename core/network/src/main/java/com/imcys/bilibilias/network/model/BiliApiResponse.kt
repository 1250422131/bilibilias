package com.imcys.bilibilias.network.model

import com.imcys.bilibilias.network.model.serializer.BiliApiResponseSerializer
import kotlinx.serialization.Serializable

@Serializable(BiliApiResponseSerializer::class)
data class BiliApiResponse<out T>(
    val code: Int,
    val data: T?,
    val result: T?,
    val message: String?,
    val ttl: Int = 0,
    @Transient
    var responseHeader: Set<Map.Entry<String, List<String>>> = setOf()
)