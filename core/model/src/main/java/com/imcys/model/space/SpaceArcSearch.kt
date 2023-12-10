package com.imcys.model.space

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class SpaceArcSearch(
    @SerialName("list")
    val list: Lists = Lists(),
    @SerialName("page")
    val page: Page = Page()
) {
    @Serializable
    data class Lists(
        @SerialName("vlist")
        val vlist: List<Vlist> = listOf(),
    ) {
        @Serializable
        data class Vlist(
            @SerialName("aid")
            val aid: Long = 0,
            @SerialName("author")
            val author: String = "",
            @SerialName("bvid")
            val bvid: String = "",
            @SerialName("created")
            val created: Long = 0,
            @SerialName("description")
            val description: String = "",
            @SerialName("length")
            val length: String = "",
            @SerialName("mid")
            val mid: Long = 0,
            @SerialName("pic")
            val pic: String = "",
            @SerialName("play")
            val play: Int = 0,
            @SerialName("title")
            val title: String = "",
            @SerialName("video_review")
            val videoReview: Int = 0
        )
    }

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    data class Page constructor(
        @JsonNames("count")
        val count: Int = 0,
        @JsonNames("pn")
        val pn: Int = 0,
        @JsonNames("ps")
        val ps: Int = 0
    )
}
