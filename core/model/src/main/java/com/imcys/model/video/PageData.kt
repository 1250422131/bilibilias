package com.imcys.model.video

import com.imcys.model.Dimension
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageData(
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("from")
    val from: String = "",
    @SerialName("page")
    val page: Int = 0,
    @SerialName("part")
    val part: String = "",
    @SerialName("vid")
    val vid: String = "",
    @SerialName("weblink")
    val weblink: String = ""
)
