package com.imcys.bilibilias.core.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    @SerialName("archive_count")
    val archiveCount: Int = 0,
    @SerialName("article_count")
    val articleCount: Int = 0,
    @SerialName("card")
    val card: Card = Card(),
    @SerialName("follower")
    val follower: Int = 0,
    @SerialName("following")
    val following: Boolean = false,
    @SerialName("like_num")
    val likeNum: Int = 0
) {
    @Serializable
    data class Card(
        @SerialName("approve")
        val approve: Boolean = false,
        @SerialName("article")
        val article: Int = 0,
        @SerialName("attention")
        val attention: Int = 0,
        @SerialName("birthday")
        val birthday: String = "",
        @SerialName("description")
        val description: String = "",
        @SerialName("DisplayRank")
        val displayRank: String = "",
        @SerialName("face")
        val face: String = "",
        @SerialName("face_nft")
        val faceNft: Int = 0,
        @SerialName("face_nft_type")
        val faceNftType: Int = 0,
        @SerialName("fans")
        val fans: Int = 0,
        @SerialName("friend")
        val friend: Int = 0,
        @SerialName("is_senior_member")
        val isSeniorMember: Int = 0,
        @SerialName("level_info")
        val levelInfo: LevelInfo = LevelInfo(),
        @SerialName("mid")
        val mid: String = "",
        @SerialName("name")
        val name: String = "",
        @SerialName("nameplate")
        val nameplate: Nameplate = Nameplate(),
        @SerialName("Official")
        val official: Official = Official(),
        @SerialName("official_verify")
        val officialVerify: OfficialVerify = OfficialVerify(),
        @SerialName("pendant")
        val pendant: Pendant = Pendant(),
        @SerialName("place")
        val place: String = "",
        @SerialName("rank")
        val rank: String = "",
        @SerialName("regtime")
        val regtime: Int = 0,
        @SerialName("sex")
        val sex: String = "",
        @SerialName("sign")
        val sign: String = "",
        @SerialName("spacesta")
        val spacesta: Int = 0,
        @SerialName("vip")
        val vip: Vip = Vip()
    ) {
        @Serializable
        data class Nameplate(
            @SerialName("condition")
            val condition: String = "",
            @SerialName("image")
            val image: String = "",
            @SerialName("image_small")
            val imageSmall: String = "",
            @SerialName("level")
            val level: String = "",
            @SerialName("name")
            val name: String = "",
            @SerialName("nid")
            val nid: Int = 0
        )

        @Serializable
        data class OfficialVerify(
            @SerialName("desc")
            val desc: String = "",
            @SerialName("type")
            val type: Int = 0
        )
    }
}
