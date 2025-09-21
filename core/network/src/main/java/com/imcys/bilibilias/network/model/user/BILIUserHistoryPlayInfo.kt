package com.imcys.bilibilias.network.model.user
import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

@Serializable
data class BILIUserHistoryPlayInfo(
    @SerialName("cursor")
    val cursor: Cursor,
    @SerialName("list")
    val list: List<ItemData>,
    @SerialName("tab")
    val tab: List<Tab>
) {
    @Serializable
    data class Cursor(
        @SerialName("business")
        val business: String,
        @SerialName("max")
        val max: Long,
        @SerialName("ps")
        val ps: Long,
        @SerialName("view_at")
        val viewAt: Long
    )

    @Serializable
    data class ItemData(
        @SerialName("author_face")
        val authorFace: String,
        @SerialName("author_mid")
        val authorMid: Long,
        @SerialName("author_name")
        val authorName: String,
        @SerialName("badge")
        val badge: String,
        @SerialName("cover")
        val cover: String,
        @SerialName("current")
        val current: String,
        @SerialName("duration")
        val duration: Long,
        @SerialName("history")
        val history: History,
        @SerialName("is_fav")
        val isFav: Long,
        @SerialName("is_finish")
        val isFinish: Long,
        @SerialName("kid")
        val kid: Long,
        @SerialName("live_status")
        val liveStatus: Long,
        @SerialName("long_title")
        val longTitle: String,
        @SerialName("new_desc")
        val newDesc: String,
        @SerialName("progress")
        val progress: Long,
        @SerialName("show_title")
        val showTitle: String,
        @SerialName("tag_name")
        val tagName: String,
        @SerialName("title")
        val title: String,
        @SerialName("total")
        val total: Long,
        @SerialName("uri")
        val uri: String,
        @SerialName("videos")
        val videos: Long,
        @SerialName("view_at")
        val viewAt: Long
    ) {
        @Serializable
        data class History(
            @SerialName("business")
            val business: String,
            @SerialName("bvid")
            val bvid: String,
            @SerialName("cid")
            val cid: Long,
            @SerialName("dt")
            val dt: Long,
            @SerialName("epid")
            val epid: Long,
            @SerialName("oid")
            val oid: Long,
            @SerialName("page")
            val page: Long,
            @SerialName("part")
            val part: String
        )
    }

    @Serializable
    data class Tab(
        @SerialName("name")
        val name: String,
        @SerialName("type")
        val type: String
    )
}

