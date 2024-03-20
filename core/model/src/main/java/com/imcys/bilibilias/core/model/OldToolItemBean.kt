package com.imcys.bilibilias.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 工具页类
 */
@Serializable
data class OldToolItemBean(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val result: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("color")
        val color: String,
        @SerialName("img_url")
        val imageUrl: String,
        @SerialName("title")
        val title: String,
        @SerialName("tool_code")
        val toolCode: Int
    )
}
