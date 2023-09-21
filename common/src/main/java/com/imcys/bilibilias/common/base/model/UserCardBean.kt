package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![B站用户卡片信息类](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/user/info.md)
 * ```json
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": {
 *         "card": {
 *             "mid": "2",
 *             "name": "碧诗",
 *             "approve": false,
 *             "sex": "男",
 *             "rank": "20000",
 *             "face": "http://i2.hdslb.com/bfs/face/ef0457addb24141e15dfac6fbf45293ccf1e32ab.jpg",
 *             "DisplayRank": "0",
 *             "regtime": 0,
 *             "spacesta": 0,
 *             "birthday": "",
 *             "place": "",
 *             "description": "",
 *             "article": 0,
 *             "attentions": [],
 *             "fans": 969999,
 *             "friend": 234,
 *             "attention": 234,
 *             "sign": "kami.im 直男过气网红 # av362830 “We Are Star Dust”",
 *             "level_info": {
 *                 "current_level": 6,
 *                 "current_min": 0,
 *                 "current_exp": 0,
 *                 "next_exp": 0
 *             },
 *             "pendant": {
 *                 "pid": 0,
 *                 "name": "",
 *                 "image": "",
 *                 "expire": 0,
 *                 "image_enhance": "",
 *                 "image_enhance_frame": ""
 *             },
 *             "nameplate": {
 *                 "nid": 10,
 *                 "name": "见习偶像",
 *                 "image": "http://i2.hdslb.com/bfs/face/e93dd9edfa7b9e18bf46fd8d71862327a2350923.png",
 *                 "image_small": "http://i2.hdslb.com/bfs/face/275b468b043ec246737ab8580a2075bee0b1263b.png",
 *                 "level": "普通勋章",
 *                 "condition": "所有自制视频总播放数\u003e=10万"
 *             },
 *             "Official": {
 *                 "role": 2,
 *                 "title": "bilibili创始人（站长）",
 *                 "desc": "",
 *                 "type": 0
 *             },
 *             "official_verify": {
 *                 "type": 0,
 *                 "desc": "bilibili创始人（站长）"
 *             },
 *             "vip": {
 *                 "type": 2,
 *                 "status": 1,
 *                 "due_date": 3896524800000,
 *                 "vip_pay_type": 0,
 *                 "theme_type": 0,
 *                 "label": {
 *                     "path": "",
 *                     "text": "十年大会员",
 *                     "label_theme": "ten_annual_vip",
 *                     "text_color": "#FFFFFF",
 *                     "bg_style": 1,
 *                     "bg_color": "#FB7299",
 *                     "border_color": ""
 *                 },
 *                 "avatar_subscript": 1,
 *                 "nickname_color": "#FB7299",
 *                 "role": 7,
 *                 "avatar_subscript_url": "http://i0.hdslb.com/bfs/vip/icon_Certification_big_member_22_3x.png",
 *                 "vipType": 2,
 *                 "vipStatus": 1
 *             }
 *         },
 *         "space": {
 *             "s_img": "http://i1.hdslb.com/bfs/space/768cc4fd97618cf589d23c2711a1d1a729f42235.png",
 *             "l_img": "http://i1.hdslb.com/bfs/space/cb1c3ef50e22b6096fde67febe863494caefebad.png"
 *         }
 *     },
 *     "following": true,
 *     "archive_count": 37,
 *     "article_count": 0,
 *     "follower": 969999,
 *     "like_num": 3547978
 * }
 * ```
 */
@Serializable
data class UserCardBean(
    @SerialName("card")
    val card: Card = Card(),
    @SerialName("space")
    val space: Space = Space()
) {
    @Serializable
    data class Card(
        @SerialName("approve")
        val approve: Boolean = false, // false
        @SerialName("article")
        val article: Int = 0, // 0
        @SerialName("attention")
        val attention: Int = 0, // 234
        @SerialName("birthday")
        val birthday: String = "",
        @SerialName("description")
        val description: String = "",
        @SerialName("DisplayRank")
        val displayRank: String = "", // 0
        @SerialName("face")
        val face: String = "", // http://i2.hdslb.com/bfs/face/ef0457addb24141e15dfac6fbf45293ccf1e32ab.jpg
        @SerialName("fans")
        val fans: Int = 0, // 969999
        @SerialName("friend")
        val friend: Int = 0, // 234
        @SerialName("level_info")
        val levelInfo: LevelInfo = LevelInfo(),
        @SerialName("mid")
        val mid: String = "", // 2
        @SerialName("name")
        val name: String = "", // 碧诗
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
        val rank: String = "", // 20000
        @SerialName("regtime")
        val regtime: Int = 0, // 0
        @SerialName("sex")
        val sex: String = "", // 男
        @SerialName("sign")
        val sign: String = "", // kami.im 直男过气网红 # av362830 “We Are Star Dust”
        @SerialName("spacesta")
        val spacesta: Int = 0, // 0
        @SerialName("vip")
        val vip: Vip = Vip()
    ) {
        @Serializable
        data class LevelInfo(
            @SerialName("current_exp")
            val currentExp: Int = 0, // 0
            @SerialName("current_level")
            val currentLevel: Int = 0, // 6
            @SerialName("current_min")
            val currentMin: Int = 0, // 0
            @SerialName("next_exp")
            val nextExp: Int = 0 // 0
        )

        @Serializable
        data class Nameplate(
            @SerialName("condition")
            val condition: String = "", // 所有自制视频总播放数>=10万
            @SerialName("image")
            val image: String = "", // http://i2.hdslb.com/bfs/face/e93dd9edfa7b9e18bf46fd8d71862327a2350923.png
            @SerialName("image_small")
            val imageSmall: String = "", // http://i2.hdslb.com/bfs/face/275b468b043ec246737ab8580a2075bee0b1263b.png
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
        data class OfficialVerify(
            @SerialName("desc")
            val desc: String = "", // bilibili创始人（站长）
            @SerialName("type")
            val type: Int = 0 // 0
        )

        @Serializable
        data class Pendant(
            @SerialName("expire")
            val expire: Int = 0, // 0
            @SerialName("image")
            val image: String = "",
            @SerialName("image_enhance")
            val imageEnhance: String = "",
            @SerialName("image_enhance_frame")
            val imageEnhanceFrame: String = "",
            @SerialName("name")
            val name: String = "",
            @SerialName("pid")
            val pid: Int = 0 // 0
        )

        @Serializable
        data class Vip(
            @SerialName("avatar_subscript")
            val avatarSubscript: Int = 0, // 1
            @SerialName("avatar_subscript_url")
            val avatarSubscriptUrl: String = "", // http://i0.hdslb.com/bfs/vip/icon_Certification_big_member_22_3x.png
            @SerialName("due_date")
            val dueDate: Long = 0, // 3896524800000
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
            @SerialName("type")
            val type: Int = 0, // 2
            @SerialName("vip_pay_type")
            val vipPayType: Int = 0, // 0
            @SerialName("vipStatus")
            val vipStatus: Int = 0, // 1
            @SerialName("vipType")
            val vipType: Int = 0 // 2
        ) {
            @Serializable
            data class Label(
                @SerialName("bg_color")
                val bgColor: String = "", // #FB7299
                @SerialName("bg_style")
                val bgStyle: Int = 0, // 1
                @SerialName("border_color")
                val borderColor: String = "",
                @SerialName("label_theme")
                val labelTheme: String = "", // ten_annual_vip
                @SerialName("path")
                val path: String = "",
                @SerialName("text")
                val text: String = "", // 十年大会员
                @SerialName("text_color")
                val textColor: String = "" // #FFFFFF
            )
        }
    }

    @Serializable
    data class Space(
        @SerialName("l_img")
        val lImg: String = "", // http://i1.hdslb.com/bfs/space/cb1c3ef50e22b6096fde67febe863494caefebad.png
        @SerialName("s_img")
        val sImg: String = "" // http://i1.hdslb.com/bfs/space/768cc4fd97618cf589d23c2711a1d1a729f42235.png
    )
}
