package com.imcys.bilibilias.network.model.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BILIUserFolderListInfo(
    @SerialName("count")
    val count: Long,
    @SerialName("list")
    val list: List<ItemData>,
) {
    @Serializable
    data class ItemData(
        @SerialName("attr")
        val attr: Long,
        @SerialName("fav_state")
        val favState: Long,
        @SerialName("fid")
        val fid: Long,
        @SerialName("id")
        val id: Long,
        @SerialName("media_count")
        val mediaCount: Long,
        @SerialName("mid")
        val mid: Long,
        @SerialName("title")
        val title: String
    )
}


@Serializable
data class BILIUserFolderDetailInfo(
    @SerialName("has_more")
    val hasMore: Boolean,
    @SerialName("medias")
    val medias: List<Media> = emptyList(),
    @SerialName("ttl")
    val ttl: Long
) {

    @Serializable
    data class Media(
        @SerialName("attr")
        val attr: Long,
        @SerialName("bv_id")
        val bvId: String,
        @SerialName("bvid")
        val bvid: String,
        @SerialName("cnt_info")
        val cntInfo: CntInfo,
        @SerialName("cover")
        val cover: String,
        @SerialName("ctime")
        val ctime: Long,
        @SerialName("duration")
        val duration: Long,
        @SerialName("fav_time")
        val favTime: Long,
        @SerialName("id")
        val id: Long,
        @SerialName("Longro")
        val Longro: String = "",
        @SerialName("link")
        val link: String = "",
        @SerialName("media_list_link")
        val mediaListLink: String,
        @SerialName("page")
        val page: Long,
        @SerialName("pubtime")
        val pubtime: Long,
        @SerialName("title")
        val title: String,
        @SerialName("type")
        val type: Long,
        @SerialName("upper")
        val upper: Upper
    ) {
        @Serializable
        data class CntInfo(
            @SerialName("collect")
            val collect: Long,
            @SerialName("danmaku")
            val danmaku: Long,
            @SerialName("play")
            val play: Long,
            @SerialName("play_switch")
            val playSwitch: Long,
            @SerialName("reply")
            val reply: Long,
            @SerialName("view_text_1")
            val viewText1: String?,
            @SerialName("vt")
            val vt: Long
        )

        @Serializable
        data class Ogv(
            @SerialName("season_id")
            val seasonId: Long,
            @SerialName("type_id")
            val typeId: Long,
            @SerialName("type_name")
            val typeName: String
        )

        @Serializable
        data class Ugc(
            @SerialName("first_cid")
            val firstCid: Long
        )

        @Serializable
        data class Upper(
            @SerialName("face")
            val face: String,
            @SerialName("jump_link")
            val jumpLink: String,
            @SerialName("mid")
            val mid: Long,
            @SerialName("name")
            val name: String
        )
    }
}