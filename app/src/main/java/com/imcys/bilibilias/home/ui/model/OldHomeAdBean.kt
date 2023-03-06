package com.imcys.bilibilias.home.ui.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OldHomeAdBean(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: List<Data>,
): Serializable {
    data class Data(
        @SerializedName("long_title")
        val longTitle: String, // awa
        @SerializedName("show_type")
        val showType: Int, // 1
        @SerializedName("title")
        val title: String, // 老王梯子
        @SerializedName("content")
        val content: String,
    ): Serializable
}