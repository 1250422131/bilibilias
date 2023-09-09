package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

/**
 * ![用户基本信息类](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/user/info.md)
 * ```json
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": {
 *         "mid": 2,
 *         "name": "碧诗",
 *         "sex": "男",
 *         "face": "https://i2.hdslb.com/bfs/face/ef0457addb24141e15dfac6fbf45293ccf1e32ab.jpg",
 *         "face_nft": 0,
 *         "face_nft_type": 0,
 *         "sign": "https://kami.im 直男过气网红 #  We Are Star Dust",
 *         "rank": 20000,
 *         "level": 6,
 *         "jointime": 0,
 *         "moral": 0,
 *         "silence": 0,
 *         "coins": 0,
 *         "fans_badge": true,
 *         "fans_medal": {
 *             "show": true,
 *             "wear": true,
 *             "medal": {
 *                 "uid": 2,
 *                 "target_id": 335115,
 *                 "medal_id": 45408,
 *                 "level": 21,
 *                 "medal_name": "伍千万",
 *                 "medal_color": 1725515,
 *                 "intimacy": 1980,
 *                 "next_intimacy": 2000,
 *                 "day_limit": 250000,
 *                 "medal_color_start": 1725515,
 *                 "medal_color_end": 5414290,
 *                 "medal_color_border": 1725515,
 *                 "is_lighted": 1,
 *                 "light_status": 1,
 *                 "wearing_status": 1,
 *                 "score": 50001980
 *             }
 *         },
 *         "official": {
 *             "role": 2,
 *             "title": "bilibili创始人（站长）",
 *             "desc": "",
 *             "type": 0
 *         },
 *         "vip": {
 *             "type": 2,
 *             "status": 1,
 *             "due_date": 3931344000000,
 *             "vip_pay_type": 0,
 *             "theme_type": 0,
 *             "label": {
 *                 "path": "",
 *                 "text": "十年大会员",
 *                 "label_theme": "ten_annual_vip",
 *                 "text_color": "#FFFFFF",
 *                 "bg_style": 1,
 *                 "bg_color": "#FB7299",
 *                 "border_color": "",
 *                 "use_img_label": true,
 *                 "img_label_uri_hans": "https://i0.hdslb.com/bfs/activity-plat/static/20220608/e369244d0b14644f5e1a06431e22a4d5/wltavwHAkL.gif",
 *                 "img_label_uri_hant": "",
 *                 "img_label_uri_hans_static": "https://i0.hdslb.com/bfs/vip/802418ff03911645648b63aa193ba67997b5a0bc.png",
 *                 "img_label_uri_hant_static": "https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/8u7iRTPE7N.png"
 *             },
 *             "avatar_subscript": 1,
 *             "nickname_color": "#FB7299",
 *             "role": 7,
 *             "avatar_subscript_url": "",
 *             "tv_vip_status": 1,
 *             "tv_vip_pay_type": 0,
 *             "tv_due_date": 2000822400
 *         },
 *         "pendant": {
 *             "pid": 32257,
 *             "name": "EveOneCat2",
 *             "image": "https://i2.hdslb.com/bfs/garb/item/488870931b1bba66da36d22848f0720480d3d79a.png",
 *             "expire": 0,
 *             "image_enhance": "https://i2.hdslb.com/bfs/garb/item/5974f17f9d96a88bafba2f6d18d647a486e88312.webp",
 *             "image_enhance_frame": "https://i2.hdslb.com/bfs/garb/item/4316a3910bb0bd6f2f1c267a3e9187f0b9fe5bd0.png"
 *         },
 *         "nameplate": {
 *             "nid": 10,
 *             "name": "见习偶像",
 *             "image": "https://i2.hdslb.com/bfs/face/e93dd9edfa7b9e18bf46fd8d71862327a2350923.png",
 *             "image_small": "https://i2.hdslb.com/bfs/face/275b468b043ec246737ab8580a2075bee0b1263b.png",
 *             "level": "普通勋章",
 *             "condition": "所有自制视频总播放数>=10万"
 *         },
 *         "user_honour_info": {
 *             "mid": 0,
 *             "colour": null,
 *             "tags": []
 *         },
 *         "is_followed": true,
 *         "top_photo": "http://i2.hdslb.com/bfs/space/cb1c3ef50e22b6096fde67febe863494caefebad.png",
 *         "theme": {},
 *         "sys_notice": {},
 *         "live_room": {
 *             "roomStatus": 1,
 *             "liveStatus": 0,
 *             "url": "https://live.bilibili.com/1024?broadcast_type=0&is_room_feed=0",
 *             "title": "试图恰鸡",
 *             "cover": "http://i0.hdslb.com/bfs/live/new_room_cover/96ee5bfd0279a0f18b190340334f43f473038288.jpg",
 *             "roomid": 1024,
 *             "roundStatus": 0,
 *             "broadcast_type": 0,
 *             "watched_show": {
 *                 "switch": true,
 *                 "num": 19,
 *                 "text_small": "19",
 *                 "text_large": "19人看过",
 *                 "icon": "https://i0.hdslb.com/bfs/live/a725a9e61242ef44d764ac911691a7ce07f36c1d.png",
 *                 "icon_location": "",
 *                 "icon_web": "https://i0.hdslb.com/bfs/live/8d9d0f33ef8bf6f308742752d13dd0df731df19c.png"
 *             }
 *         },
 *         "birthday": "09-19",
 *         "school": {
 *             "name": ""
 *         },
 *         "profession": {
 *             "name": "",
 *             "department": "",
 *             "title": "",
 *             "is_show": 0
 *         },
 *         "tags": null,
 *         "series": {
 *             "user_upgrade_status": 3,
 *             "show_upgrade_window": false
 *         },
 *         "is_senior_member": 0,
 *         "mcn_info": null,
 *         "gaia_res_type": 0,
 *         "gaia_data": null,
 *         "is_risk": false,
 *         "elec": {
 *             "show_info": {
 *                 "show": true,
 *                 "state": 1,
 *                 "title": "",
 *                 "icon": "",
 *                 "jump_url": "?oid=2"
 *             }
 *         },
 *         "contract": {
 *             "is_display": false,
 *             "is_follow_display": false
 *         }
 *     }
 * }
 * ```
 */
@Serializable
data class UserBaseBean(
    @SerialName("birthday")
    val birthday: String = "", // 09-19
    @SerialName("coins")
    val coins: Float = 0f, // 0
    @SerialName("contract")
    val contract: Contract = Contract(),
    @SerialName("elec")
    val elec: Elec = Elec(),
    @SerialName("face")
    val face: String = "", // https://i2.hdslb.com/bfs/face/ef0457addb24141e15dfac6fbf45293ccf1e32ab.jpg
    @SerialName("face_nft")
    val faceNft: Int = 0, // 0
    @SerialName("face_nft_type")
    val faceNftType: Int = 0, // 0
    @SerialName("fans_badge")
    val fansBadge: Boolean = false, // true
    @SerialName("fans_medal")
    val fansMedal: FansMedal = FansMedal(),
    @SerialName("gaia_res_type")
    val gaiaResType: Int = 0, // 0
    @SerialName("is_followed")
    val isFollowed: Boolean = false, // true
    @SerialName("is_risk")
    val isRisk: Boolean = false, // false
    @SerialName("is_senior_member")
    val isSeniorMember: Int = 0, // 0
    @SerialName("jointime")
    val jointime: Int = 0, // 0
    @SerialName("level")
    val level: Int = 0, // 6
    @SerialName("live_room")
    val liveRoom: LiveRoom = LiveRoom(),
    @SerialName("mid")
    val mid: Int = 0, // 2
    @SerialName("moral")
    val moral: Int = 0, // 0
    @SerialName("name")
    val name: String = "", // 碧诗
    @SerialName("nameplate")
    val nameplate: Nameplate = Nameplate(),
    @SerialName("official")
    val official: Official = Official(),
    @SerialName("pendant")
    val pendant: Pendant = Pendant(),
    @SerialName("profession")
    val profession: Profession = Profession(),
    @SerialName("rank")
    val rank: Int = 0, // 20000
    @SerialName("school")
    val school: School = School(),
    @SerialName("series")
    val series: Series = Series(),
    @SerialName("sex")
    val sex: String = "", // 男
    @SerialName("sign")
    val sign: String = "", // https://kami.im 直男过气网红 #  We Are Star Dust
    @SerialName("silence")
    val silence: Int = 0, // 0
    @SerialName("top_photo")
    val topPhoto: String = "", // http://i2.hdslb.com/bfs/space/cb1c3ef50e22b6096fde67febe863494caefebad.png
    @SerialName("user_honour_info")
    val userHonourInfo: UserHonourInfo = UserHonourInfo(),
    @SerialName("vip")
    val vip: Vip = Vip()
) {
    @Serializable
    data class Contract(
        @SerialName("is_display")
        val isDisplay: Boolean = false, // false
        @SerialName("is_follow_display")
        val isFollowDisplay: Boolean = false // false
    )

    @Serializable
    data class Elec(
        @SerialName("show_info")
        val showInfo: ShowInfo = ShowInfo()
    ) {
        @Serializable
        data class ShowInfo(
            @SerialName("icon")
            val icon: String = "",
            @SerialName("jump_url")
            val jumpUrl: String = "", // ?oid=2
            @SerialName("show")
            val show: Boolean = false, // true
            @SerialName("state")
            val state: Int = 0, // 1
            @SerialName("title")
            val title: String = ""
        )
    }

    @Serializable
    data class FansMedal(
        @SerialName("medal")
        val medal: Medal = Medal(),
        @SerialName("show")
        val show: Boolean = false, // true
        @SerialName("wear")
        val wear: Boolean = false // true
    ) {
        @Serializable
        data class Medal(
            @SerialName("day_limit")
            val dayLimit: Int = 0, // 250000
            @SerialName("intimacy")
            val intimacy: Int = 0, // 1980
            @SerialName("is_lighted")
            val isLighted: Int = 0, // 1
            @SerialName("level")
            val level: Int = 0, // 21
            @SerialName("light_status")
            val lightStatus: Int = 0, // 1
            @SerialName("medal_color")
            val medalColor: Int = 0, // 1725515
            @SerialName("medal_color_border")
            val medalColorBorder: Int = 0, // 1725515
            @SerialName("medal_color_end")
            val medalColorEnd: Int = 0, // 5414290
            @SerialName("medal_color_start")
            val medalColorStart: Int = 0, // 1725515
            @SerialName("medal_id")
            val medalId: Int = 0, // 45408
            @SerialName("medal_name")
            val medalName: String = "", // 伍千万
            @SerialName("next_intimacy")
            val nextIntimacy: Int = 0, // 2000
            @SerialName("score")
            val score: Int = 0, // 50001980
            @SerialName("target_id")
            val targetId: Int = 0, // 335115
            @SerialName("uid")
            val uid: Int = 0, // 2
            @SerialName("wearing_status")
            val wearingStatus: Int = 0 // 1
        )
    }

    @Serializable
    data class LiveRoom(
        @SerialName("broadcast_type")
        val broadcastType: Int = 0, // 0
        @SerialName("cover")
        val cover: String = "", // http://i0.hdslb.com/bfs/live/new_room_cover/96ee5bfd0279a0f18b190340334f43f473038288.jpg
        @SerialName("liveStatus")
        val liveStatus: Int = 0, // 0
        @SerialName("roomStatus")
        val roomStatus: Int = 0, // 1
        @SerialName("roomid")
        val roomid: Int = 0, // 1024
        @SerialName("roundStatus")
        val roundStatus: Int = 0, // 0
        @SerialName("title")
        val title: String = "", // 试图恰鸡
        @SerialName("url")
        val url: String = "", // https://live.bilibili.com/1024?broadcast_type=0&is_room_feed=0
        @SerialName("watched_show")
        val watchedShow: WatchedShow = WatchedShow()
    ) {
        @Serializable
        data class WatchedShow(
            @SerialName("icon")
            val icon: String = "", // https://i0.hdslb.com/bfs/live/a725a9e61242ef44d764ac911691a7ce07f36c1d.png
            @SerialName("icon_location")
            val iconLocation: String = "",
            @SerialName("icon_web")
            val iconWeb: String = "", // https://i0.hdslb.com/bfs/live/8d9d0f33ef8bf6f308742752d13dd0df731df19c.png
            @SerialName("num")
            val num: Int = 0, // 19
            @SerialName("switch")
            val switch: Boolean = false, // true
            @SerialName("text_large")
            val textLarge: String = "", // 19人看过
            @SerialName("text_small")
            val textSmall: String = "" // 19
        )
    }

    @Serializable
    data class Nameplate(
        @SerialName("condition")
        val condition: String = "", // 所有自制视频总播放数>=10万
        @SerialName("image")
        val image: String = "", // https://i2.hdslb.com/bfs/face/e93dd9edfa7b9e18bf46fd8d71862327a2350923.png
        @SerialName("image_small")
        val imageSmall: String = "", // https://i2.hdslb.com/bfs/face/275b468b043ec246737ab8580a2075bee0b1263b.png
        @SerialName("level")
        val level: String = "", // 普通勋章
        @SerialName("name")
        val name: String = "", // 见习偶像
        @SerialName("nid")
        val nid: Int = 0 // 10
    )

    @Serializable
    data class Official(
        @SerialName("desc")
        val desc: String = "",
        @SerialName("role")
        val role: Int = 0, // 2
        @SerialName("title")
        val title: String = "", // bilibili创始人（站长）
        @SerialName("type")
        val type: Int = 0 // 0
    )

    @Serializable
    data class Pendant(
        @SerialName("expire")
        val expire: Int = 0, // 0
        @SerialName("image")
        val image: String = "", // https://i2.hdslb.com/bfs/garb/item/488870931b1bba66da36d22848f0720480d3d79a.png
        @SerialName("image_enhance")
        val imageEnhance: String = "", // https://i2.hdslb.com/bfs/garb/item/5974f17f9d96a88bafba2f6d18d647a486e88312.webp
        @SerialName("image_enhance_frame")
        val imageEnhanceFrame: String = "", // https://i2.hdslb.com/bfs/garb/item/4316a3910bb0bd6f2f1c267a3e9187f0b9fe5bd0.png
        @SerialName("name")
        val name: String = "", // EveOneCat2
        @SerialName("pid")
        val pid: Int = 0 // 32257
    )

    @Serializable
    data class Profession(
        @SerialName("department")
        val department: String = "",
        @SerialName("is_show")
        val isShow: Int = 0, // 0
        @SerialName("name")
        val name: String = "",
        @SerialName("title")
        val title: String = ""
    )

    @Serializable
    data class School(
        @SerialName("name")
        val name: String = ""
    )

    @Serializable
    data class Series(
        @SerialName("show_upgrade_window")
        val showUpgradeWindow: Boolean = false, // false
        @SerialName("user_upgrade_status")
        val userUpgradeStatus: Int = 0 // 3
    )

    @Serializable
    data class UserHonourInfo(
        @SerialName("mid")
        val mid: Long = 0, // 0
    )

    @Serializable
    data class Vip(
        @SerialName("avatar_subscript")
        val avatarSubscript: Int = 0, // 1
        @SerialName("avatar_subscript_url")
        val avatarSubscriptUrl: String = "",
        @SerialName("due_date")
        val dueDate: Long = 0, // 3931344000000
        @SerialName("label")
        val label: Label = Label(),
        @SerialName("nickname_color")
        val nicknameColor: String = "", // #FB7299
        @SerialName("role")
        val role: Int = 0, // 7
        @SerialName("status")
        val status: Int = 0, // 1
        @SerialName("theme_type")
        val themeType: Int = 0, // 0
        @SerialName("tv_due_date")
        val tvDueDate: Int = 0, // 2000822400
        @SerialName("tv_vip_pay_type")
        val tvVipPayType: Int = 0, // 0
        @SerialName("tv_vip_status")
        val tvVipStatus: Int = 0, // 1
        @SerialName("type")
        val type: Int = 0, // 2
        @SerialName("vip_pay_type")
        val vipPayType: Int = 0 // 0
    ) {
        @Serializable
        data class Label(
            @SerialName("bg_color")
            val bgColor: String = "", // #FB7299
            @SerialName("bg_style")
            val bgStyle: Int = 0, // 1
            @SerialName("border_color")
            val borderColor: String = "",
            @SerialName("img_label_uri_hans")
            val imgLabelUriHans: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220608/e369244d0b14644f5e1a06431e22a4d5/wltavwHAkL.gif
            @SerialName("img_label_uri_hans_static")
            val imgLabelUriHansStatic: String = "", // https://i0.hdslb.com/bfs/vip/802418ff03911645648b63aa193ba67997b5a0bc.png
            @SerialName("img_label_uri_hant")
            val imgLabelUriHant: String = "",
            @SerialName("img_label_uri_hant_static")
            val imgLabelUriHantStatic: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/8u7iRTPE7N.png
            @SerialName("label_theme")
            val labelTheme: String = "", // ten_annual_vip
            @SerialName("path")
            val path: String = "",
            @SerialName("text")
            val text: String = "", // 十年大会员
            @SerialName("text_color")
            val textColor: String = "", // #FFFFFF
            @SerialName("use_img_label")
            val useImgLabel: Boolean = false // true
        )
    }

    @Transient
    val isVip = vip.status == 1
}
