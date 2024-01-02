package com.imcys.model.space

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("category")
    val category: Int = 0,
    @SerialName("cover")
    val cover: String = "",
    @SerialName("creator")
    val creator: String = "",
    @SerialName("ctime")
    val ctime: Int = 0,
    @SerialName("description")
    val description: String = "",
    @SerialName("keywords")
    val keywords: List<String> = emptyList(),
    @SerialName("last_update_ts")
    val lastUpdateTs: Int = 0,
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("mtime")
    val mtime: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("raw_keywords")
    val rawKeywords: String = "",
    @SerialName("series_id")
    val seriesId: Long = 0,
    @SerialName("state")
    val state: Int = 0,
    @SerialName("total")
    val total: Int = 0
)
