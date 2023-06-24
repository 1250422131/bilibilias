package com.imcys.bilibilias.tool_livestream.ui.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LiveRoomDataBean(
    @SerializedName("code")
    val code: Int, // 0
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String, // ok
    @SerializedName("msg")
    val msg: String // ok
) : Serializable {
    data class Data(
        @SerializedName("allow_change_area_time")
        val allowChangeAreaTime: Int, // 0
        @SerializedName("allow_upload_cover_time")
        val allowUploadCoverTime: Int, // 0
        @SerializedName("area_id")
        val areaId: Int, // 236
        @SerializedName("area_name")
        val areaName: String, // 主机游戏
        @SerializedName("area_pendants")
        val areaPendants: String,
        @SerializedName("attention")
        val attention: Int, // 11661
        @SerializedName("background")
        val background: String,
        @SerializedName("battle_id")
        val battleId: Int, // 0
        @SerializedName("description")
        val description: String, // <p>每天早晨9点-15点，晚上7点-11点。微博@无糖恋爱了嘛</p>
        @SerializedName("hot_words")
        val hotWords: List<String>,
        @SerializedName("hot_words_status")
        val hotWordsStatus: Int, // 0
        @SerializedName("is_anchor")
        val isAnchor: Int, // 0
        @SerializedName("is_portrait")
        val isPortrait: Boolean, // false
        @SerializedName("is_strict_room")
        val isStrictRoom: Boolean, // false
        @SerializedName("keyframe")
        val keyframe: String, // https://i0.hdslb.com/bfs/live-key-frame/keyframe012515370000007321111xjkfk.jpg
        @SerializedName("live_status")
        val liveStatus: Int, // 1
        @SerializedName("live_time")
        val liveTime: String, // 2023-01-25 12:38:12
        @SerializedName("new_pendants")
        val newPendants: NewPendants,
        @SerializedName("old_area_id")
        val oldAreaId: Int, // 1
        @SerializedName("online")
        val online: Int, // 5453
        @SerializedName("parent_area_id")
        val parentAreaId: Int, // 6
        @SerializedName("parent_area_name")
        val parentAreaName: String, // 单机游戏
        @SerializedName("pendants")
        val pendants: String,
        @SerializedName("pk_id")
        val pkId: Int, // 0
        @SerializedName("pk_status")
        val pkStatus: Int, // 0
        @SerializedName("room_id")
        val roomId: Long, // 732111
        @SerializedName("room_silent_level")
        val roomSilentLevel: Int, // 0
        @SerializedName("room_silent_second")
        val roomSilentSecond: Int, // 0
        @SerializedName("room_silent_type")
        val roomSilentType: String,
        @SerializedName("short_id")
        val shortId: Int, // 0
        @SerializedName("studio_info")
        val studioInfo: StudioInfo,
        @SerializedName("tags")
        val tags: String, // 恐怖游戏,暴雪游戏
        @SerializedName("title")
        val title: String, // 空洞骑士
        @SerializedName("uid")
        val uid: Long, // 11947955
        @SerializedName("up_session")
        val upSession: String, // 335208088485964751
        @SerializedName("user_cover")
        val userCover: String, // https://i0.hdslb.com/bfs/live/new_room_cover/12a64e8c2235edafb28d04125b61e526c94e575d.jpg
        @SerializedName("verify")
        val verify: String
    ) : Serializable {
        data class NewPendants(
            @SerializedName("badge")
            val badge: Badge,
            @SerializedName("frame")
            val frame: Frame,
            @SerializedName("mobile_badge")
            val mobileBadge: Any, // null
            @SerializedName("mobile_frame")
            val mobileFrame: MobileFrame
        ) : Serializable {
            data class Badge(
                @SerializedName("desc")
                val desc: String, // bilibili 直播高能主播
                @SerializedName("name")
                val name: String, // v_person
                @SerializedName("position")
                val position: Int, // 3
                @SerializedName("value")
                val value: String
            ) : Serializable

            data class Frame(
                @SerializedName("area")
                val area: Int, // 0
                @SerializedName("area_old")
                val areaOld: Int, // 0
                @SerializedName("bg_color")
                val bgColor: String,
                @SerializedName("bg_pic")
                val bgPic: String,
                @SerializedName("desc")
                val desc: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("position")
                val position: Int, // 0
                @SerializedName("use_old_area")
                val useOldArea: Boolean, // false
                @SerializedName("value")
                val value: String
            ) : Serializable

            data class MobileFrame(
                @SerializedName("area")
                val area: Int, // 0
                @SerializedName("area_old")
                val areaOld: Int, // 0
                @SerializedName("bg_color")
                val bgColor: String,
                @SerializedName("bg_pic")
                val bgPic: String,
                @SerializedName("desc")
                val desc: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("position")
                val position: Int, // 0
                @SerializedName("use_old_area")
                val useOldArea: Boolean, // false
                @SerializedName("value")
                val value: String
            ) : Serializable
        }

        data class StudioInfo(
            @SerializedName("master_list")
            val masterList: List<Any>,
            @SerializedName("status")
            val status: Int // 0
        ) : Serializable
    }
}