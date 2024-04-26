package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OldHomeAdBean(
    @SerialName("code")
    val code: Int = 0,
    @SerialName("data")
    val `data`: List<Data> = emptyList(),
) {
    @Serializable
    data class Data(
        @SerialName("long_title")
        val longTitle: String, // awa
        @SerialName("show_type")
        val showType: Int, // 1
        @SerialName("title")
        val title: String, // 老王梯子
        @SerialName("content")
        val content: String,
    )
}