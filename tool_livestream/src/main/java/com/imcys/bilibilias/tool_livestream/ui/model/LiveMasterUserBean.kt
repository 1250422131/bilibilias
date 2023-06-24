package com.imcys.bilibilias.tool_livestream.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class LiveMasterUserBean(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String,
    @SerializedName("msg")
    val msg: String
) : Serializable {
    data class Data(

        @SerializedName("exp")
        val exp: Exp,
        @SerializedName("follower_num")
        val followerNum: Int, // 11663
        @SerializedName("glory_count")
        val gloryCount: Int, // 1
        @SerializedName("info")
        val info: Info,
        @SerializedName("link_group_num")
        val linkGroupNum: Int, // 0
        @SerializedName("medal_name")
        val medalName: String, // 無糖
        @SerializedName("pendant")
        val pendant: String,
        @SerializedName("room_id")
        val roomId: Int, // 732111
        @SerializedName("room_news")
        val roomNews: RoomNews
    ) : Serializable {
        data class Exp(
            @SerializedName("master_level")
            val masterLevel: MasterLevel
        ) : Serializable {
            data class MasterLevel(
                @SerializedName("color")
                val color: Int, // 10512625
                @SerializedName("current")
                val current: List<Int>,
                @SerializedName("level")
                val level: Int, // 27
                @SerializedName("next")
                val next: List<Int>
            ) : Serializable
        }

        data class Info(
            @SerializedName("face")
            val face: String, // https://i2.hdslb.com/bfs/face/c1264943c1f4763d139394e35dabb6e4bb7e861d.jpg
            @SerializedName("gender")
            val gender: Int, // 1
            @SerializedName("official_verify")
            val officialVerify: OfficialVerify,
            @SerializedName("uid")
            val uid: Long, // 11947955
            @SerializedName("uname")
            val uname: String // 无糖恋爱了嘛
        ) : Serializable {
            data class OfficialVerify(
                @SerializedName("desc")
                val desc: String, // bilibili个人认证:bilibili 直播高能主播
                @SerializedName("type")
                val type: Int // 0
            )
        }

        data class RoomNews(
            @SerializedName("content")
            val content: String,
            @SerializedName("ctime")
            val ctime: String, // 2020-10-14 07:57:29
            @SerializedName("ctime_text")
            val ctimeText: String // 2020-10-14
        ) : Serializable
    }
}