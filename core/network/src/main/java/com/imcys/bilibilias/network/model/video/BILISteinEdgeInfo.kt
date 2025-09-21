package com.imcys.bilibilias.network.model.video

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BILISteinEdgeInfo(
    @SerialName("buvid")
    val buvid: String,
    @SerialName("edge_id")
    val edgeId: Long,
    @SerialName("edges")
    val edges: Edges,
    @SerialName("is_leaf")
    val isLeaf: Long,
    @SerialName("preload")
    val preload: Preload,
    @SerialName("story_list")
    val storyList: List<Story>,
    @SerialName("title")
    val title: String
) {
    @Serializable
    data class Edges(
        @SerialName("dimension")
        val dimension: Dimension,
        @SerialName("questions")
        val questions: List<Question>,
        @SerialName("skin")
        val skin: Skin
    ) {
        @Serializable
        data class Dimension(
            @SerialName("height")
            val height: Long,
            @SerialName("rotate")
            val rotate: Long,
            @SerialName("sar")
            val sar: String,
            @SerialName("width")
            val width: Long
        )

        @Serializable
        data class Question(
            @SerialName("choices")
            val choices: List<Choice>,
            @SerialName("duration")
            val duration: Long,
            @SerialName("id")
            val id: Long,
            @SerialName("pause_video")
            val pauseVideo: Long,
            @SerialName("start_time_r")
            val startTimeR: Long,
            @SerialName("title")
            val title: String,
            @SerialName("type")
            val type: Long
        ) {
            @Serializable
            data class Choice(
                @SerialName("cid")
                val cid: Long,
                @SerialName("condition")
                val condition: String,
                @SerialName("id")
                val id: Long,
                @SerialName("is_default")
                val isDefault: Long?,
                @SerialName("native_action")
                val nativeAction: String?,
                @SerialName("option")
                val option: String?,
                @SerialName("platform_action")
                val platformAction: String?
            )
        }

        @Serializable
        data class Skin(
            @SerialName("choice_image")
            val choiceImage: String,
            @SerialName("progressbar_color")
            val progressbarColor: String,
            @SerialName("progressbar_shadow_color")
            val progressbarShadowColor: String,
            @SerialName("title_shadow_color")
            val titleShadowColor: String,
            @SerialName("title_shadow_offset_y")
            val titleShadowOffsetY: Double,
            @SerialName("title_text_color")
            val titleTextColor: String
        )
    }

    @Serializable
    data class Preload(
        @SerialName("video")
        val video: List<Video>
    ) {
        @Serializable
        data class Video(
            @SerialName("aid")
            val aid: Long,
            @SerialName("cid")
            val cid: Long
        )
    }

    @Serializable
    data class Story(
        @SerialName("cid")
        val cid: Long,
        @SerialName("cover")
        val cover: String,
        @SerialName("cursor")
        val cursor: Long,
        @SerialName("edge_id")
        val edgeId: Long,
        @SerialName("is_current")
        val isCurrent: Long?,
        @SerialName("node_id")
        val nodeId: Long,
        @SerialName("start_pos")
        val startPos: Long,
        @SerialName("title")
        val title: String
    )
}