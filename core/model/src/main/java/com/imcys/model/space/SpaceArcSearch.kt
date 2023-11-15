package com.imcys.model.space

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpaceArcSearch(
    @SerialName("episodic_button")
    val episodicButton: EpisodicButton = EpisodicButton(),
    @SerialName("list")
    val list: Lists = Lists(),
    @SerialName("page")
    val page: Page = Page()
) {
    @Serializable
    data class EpisodicButton(
        @SerialName("text")
        val text: String = "",
        @SerialName("uri")
        val uri: String = ""
    )

    @Serializable
    data class Lists(
        @SerialName("tlist")
        val tlist: Tlist = Tlist(),
        @SerialName("vlist")
        val vlist: List<Vlist> = listOf(),
    ) {
        @Serializable
        data class Tlist(
            @SerialName("1")
            val x1: X = X(),
            @SerialName("160")
            val x160: X = X(),
            @SerialName("211")
            val x211: X = X(),
            @SerialName("3")
            val x3: X = X(),
            @SerialName("4")
            val x4: X = X()
        ) {
            @Serializable
            data class X(
                @SerialName("count")
                val count: Int = 0,
                @SerialName("name")
                val name: String = "",
                @SerialName("tid")
                val tid: Int = 0
            )
        }

        @Serializable
        data class Vlist(
            @SerialName("aid")
            val aid: Int = 0,
            @SerialName("author")
            val author: String = "",
            @SerialName("bvid")
            val bvid: String = "",
            @SerialName("comment")
            val comment: Int = 0,
            @SerialName("copyright")
            val copyright: String = "",
            @SerialName("created")
            val created: Int = 0,
            @SerialName("description")
            val description: String = "",
            @SerialName("hide_click")
            val hideClick: Boolean = false,
            @SerialName("is_pay")
            val isPay: Int = 0,
            @SerialName("is_steins_gate")
            val isSteinsGate: Int = 0,
            @SerialName("is_union_video")
            val isUnionVideo: Int = 0,
            @SerialName("length")
            val length: String = "",
            @SerialName("mid")
            val mid: Int = 0,
            @SerialName("pic")
            val pic: String = "",
            @SerialName("play")
            val play: Int = 0,
            @SerialName("review")
            val review: Int = 0,
            @SerialName("subtitle")
            val subtitle: String = "",
            @SerialName("title")
            val title: String = "",
            @SerialName("typeid")
            val typeid: Int = 0,
            @SerialName("video_review")
            val videoReview: Int = 0
        )
    }

    @Serializable
    data class Page(
        @SerialName("count")
        val count: Int = 0,
        @SerialName("pn")
        val pn: Int = 0,
        @SerialName("ps")
        val ps: Int = 0
    )
}
