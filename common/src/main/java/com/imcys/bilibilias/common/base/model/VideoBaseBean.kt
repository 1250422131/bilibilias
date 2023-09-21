package com.imcys.bilibilias.common.base.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![获取视频详细信息(web端)](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/info.md#%E8%8E%B7%E5%8F%96%E8%A7%86%E9%A2%91%E8%AF%A6%E7%BB%86%E4%BF%A1%E6%81%AFweb%E7%AB%AF)
 * ```
 * {
 *   "code": 0,
 *   "message": "0",
 *   "ttl": 1,
 *   "data": {
 *     "bvid": "BV117411r7R1",
 *     "aid": 85440373,
 *     "videos": 1,
 *     "tid": 28,
 *     "tname": "原创音乐",
 *     "copyright": 1,
 *     "pic": "http://i1.hdslb.com/bfs/archive/ea0dd34bf41e23a68175680a00e3358cd249105f.jpg",
 *     "title": "当我给拜年祭的快板加了电音配乐…",
 *     "pubdate": 1580377255,
 *     "ctime": 1580212263,
 *     "desc": "【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！\n【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？\n\n原版：av78977080\n编曲（配乐）：Crazy Bucket\n人声（配音）：Warma/谢拉\n曲绘：四迹/Warma\n动画：四迹/Crazy Bucket\n剧本：Mokurei-木灵君\n音频后期：DMYoung/纳兰寻风/Crazy Bucket\n包装：破晓天",
 *     "desc_v2": [
 *       {
 *         "raw_text": "【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！\n【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？\n\n原版：av78977080\n编曲（配乐）：Crazy Bucket\n人声（配音）：Warma/谢拉\n曲绘：四迹/Warma\n动画：四迹/Crazy Bucket\n剧本：Mokurei-木灵君\n音频后期：DMYoung/纳兰寻风/Crazy Bucket\n包装：破晓天",
 *         "type": 1,
 *         "biz_id": 0
 *       }
 *     ],
 *     "state": 0,
 *     "duration": 486,
 *     "mission_id": 11838,
 *     "rights": {
 *       "bp": 0,
 *       "elec": 0,
 *       "download": 1,
 *       "movie": 0,
 *       "pay": 0,
 *       "hd5": 1,
 *       "no_reprint": 1,
 *       "autoplay": 1,
 *       "ugc_pay": 0,
 *       "is_cooperation": 1,
 *       "ugc_pay_preview": 0,
 *       "no_background": 0,
 *       "clean_mode": 0,
 *       "is_stein_gate": 0,
 *       "is_360": 0,
 *       "no_share": 0,
 *       "arc_pay": 0,
 *       "free_watch": 0
 *     },
 *     "owner": {
 *       "mid": 66606350,
 *       "name": "Crazy_Bucket_陈楒潼",
 *       "face": "http://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg"
 *     },
 *     "stat": {
 *       "aid": 85440373,
 *       "view": 2270927,
 *       "danmaku": 11839,
 *       "reply": 2619,
 *       "favorite": 58725,
 *       "coin": 71048,
 *       "share": 9398,
 *       "now_rank": 0,
 *       "his_rank": 55,
 *       "like": 155954,
 *       "dislike": 0,
 *       "evaluation": "",
 *       "argue_msg": ""
 *     },
 *     "dynamic": "进来就出不去了！！！\n#全民音乐UP主##CB##warma##电音##快板##拜年祭##诸神的奥运##编曲##Remix#",
 *     "cid": 146044693,
 *     "dimension": {
 *       "width": 1920,
 *       "height": 1080,
 *       "rotate": 0
 *     },
 *     "premiere": null,
 *     "teenage_mode": 0,
 *     "is_chargeable_season": false,
 *     "is_story": false,
 *     "no_cache": false,
 *     "pages": [
 *       {
 *         "cid": 146044693,
 *         "page": 1,
 *         "from": "vupload",
 *         "part": "建议改成：建议改成：诸 神 的 电 音 节（不是）",
 *         "duration": 486,
 *         "vid": "",
 *         "weblink": "",
 *         "dimension": {
 *           "width": 1920,
 *           "height": 1080,
 *           "rotate": 0
 *         }
 *       }
 *     ],
 *     "subtitle": {
 *       "allow_submit": false,
 *       "list": []
 *     },
 *     "staff": [
 *       {
 *         "mid": 66606350,
 *         "title": "UP主",
 *         "name": "Crazy_Bucket_陈楒潼",
 *         "face": "http://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg",
 *         "vip": {
 *           "type": 2,
 *           "status": 1,
 *           "due_date": 1674403200000,
 *           "vip_pay_type": 0,
 *           "theme_type": 0,
 *           "label": {
 *             "path": "",
 *             "text": "年度大会员",
 *             "label_theme": "annual_vip",
 *             "text_color": "#FFFFFF",
 *             "bg_style": 1,
 *             "bg_color": "#FB7299",
 *             "border_color": "",
 *             "use_img_label": true,
 *             "img_label_uri_hans": "",
 *             "img_label_uri_hant": "",
 *             "img_label_uri_hans_static": "https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png",
 *             "img_label_uri_hant_static": "https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"
 *           },
 *           "avatar_subscript": 1,
 *           "nickname_color": "#FB7299",
 *           "role": 3,
 *           "avatar_subscript_url": "",
 *           "tv_vip_status": 0,
 *           "tv_vip_pay_type": 0
 *         },
 *         "official": {
 *           "role": 1,
 *           "title": "bilibili 知名音乐UP主",
 *           "desc": "",
 *           "type": 0
 *         },
 *         "follower": 646111,
 *         "label_style": 0
 *       },
 *       {
 *         "mid": 53456,
 *         "title": "曲绘",
 *         "name": "Warma",
 *         "face": "http://i2.hdslb.com/bfs/face/c1bbee6d255f1e7fc434e9930f0f288c8b24293a.jpg",
 *         "vip": {
 *           "type": 2,
 *           "status": 1,
 *           "due_date": 1706198400000,
 *           "vip_pay_type": 0,
 *           "theme_type": 0,
 *           "label": {
 *             "path": "",
 *             "text": "年度大会员",
 *             "label_theme": "annual_vip",
 *             "text_color": "#FFFFFF",
 *             "bg_style": 1,
 *             "bg_color": "#FB7299",
 *             "border_color": "",
 *             "use_img_label": true,
 *             "img_label_uri_hans": "",
 *             "img_label_uri_hant": "",
 *             "img_label_uri_hans_static": "https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png",
 *             "img_label_uri_hant_static": "https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png"
 *           },
 *           "avatar_subscript": 1,
 *           "nickname_color": "#FB7299",
 *           "role": 3,
 *           "avatar_subscript_url": "",
 *           "tv_vip_status": 0,
 *           "tv_vip_pay_type": 0
 *         },
 *         "official": {
 *           "role": 1,
 *           "title": "bilibili 知名UP主",
 *           "desc": "",
 *           "type": 0
 *         },
 *         "follower": 3670591,
 *         "label_style": 0
 *       }
 *     ],
 *     "is_season_display": false,
 *     "user_garb": {
 *       "url_image_ani_cut": ""
 *     },
 *     "honor_reply": {
 *       "honor": [
 *         {
 *           "aid": 85440373,
 *           "type": 2,
 *           "desc": "第45期每周必看",
 *           "weekly_recommend_num": 45
 *         },
 *         {
 *           "aid": 85440373,
 *           "type": 3,
 *           "desc": "全站排行榜最高第55名",
 *           "weekly_recommend_num": 0
 *         },
 *         {
 *           "aid": 85440373,
 *           "type": 4,
 *           "desc": "热门",
 *           "weekly_recommend_num": 0
 *         }
 *       ]
 *     },
 *     "like_icon": ""
 *   }
 * }
 * ```
 */
@Serializable
data class VideoBaseBean(
    @SerialName("redirect_url")
    val redirectUrl: String? = null,
    @SerialName("aid")
    val aid: Long = 0, // 85440373
    @SerialName("bvid")
    val bvid: String = "", // BV117411r7R1
    @SerialName("cid")
    val cid: Long = 0, // 146044693
    @SerialName("copyright")
    val copyright: Int = 0, // 1
    @SerialName("ctime")
    val ctime: Long = 0, // 1580212263
    @SerialName("desc")
    val desc: String = "", // 【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？原版：av78977080编曲（配乐）：Crazy Bucket人声（配音）：Warma/谢拉曲绘：四迹/Warma动画：四迹/Crazy Bucket剧本：Mokurei-木灵君音频后期：DMYoung/纳兰寻风/Crazy Bucket包装：破晓天
    @SerialName("desc_v2")
    val descV2: List<DescV2> = listOf(),
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("duration")
    val duration: Int = 0, // 486
    @SerialName("dynamic")
    val `dynamic`: String = "", // 进来就出不去了！！！#全民音乐UP主##CB##warma##电音##快板##拜年祭##诸神的奥运##编曲##Remix#
    @SerialName("honor_reply")
    val honorReply: HonorReply = HonorReply(),
    @SerialName("is_chargeable_season")
    val isChargeableSeason: Boolean = false, // false
    @SerialName("is_season_display")
    val isSeasonDisplay: Boolean = false, // false
    @SerialName("is_story")
    val isStory: Boolean = false, // false
    @SerialName("like_icon")
    val likeIcon: String = "",
    @SerialName("mission_id")
    val missionId: Int = 0, // 11838
    @SerialName("no_cache")
    val noCache: Boolean = false, // false
    @SerialName("owner")
    val owner: Owner = Owner(),
    @SerialName("pages")
    val pages: List<Page> = listOf(),
    @SerialName("pic")
    val pic: String = "", // http://i1.hdslb.com/bfs/archive/ea0dd34bf41e23a68175680a00e3358cd249105f.jpg
    @SerialName("pubdate")
    val pubdate: Long = 0, // 1580377255
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("staff")
    val staff: List<Staff> = listOf(),
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("state")
    val state: Int = 0, // 0
    @SerialName("subtitle")
    val subtitle: Subtitle = Subtitle(),
    @SerialName("teenage_mode")
    val teenageMode: Int = 0, // 0
    @SerialName("tid")
    val tid: Int = 0, // 28
    @SerialName("title")
    val title: String = "", // 当我给拜年祭的快板加了电音配乐…
    @SerialName("tname")
    val tname: String = "", // 原创音乐
    @SerialName("user_garb")
    val userGarb: UserGarb = UserGarb(),
    @SerialName("videos")
    val videos: Int = 0 // 1
) {
    @Serializable
    data class DescV2(
        @SerialName("biz_id")
        val bizId: Int = 0, // 0
        @SerialName("raw_text")
        val rawText: String = "", // 【CB想说的】看完拜年祭之后最爱的一个节目！给有快板的部分简单加了一些不同风格的配乐hhh，感谢沃玛画的我！太可爱了哈哈哈哈哈哈哈！！！【Warma想说的】我画了打碟的CB，画风为了还原原版视频所以参考了四迹老师的画风，四迹老师的画真的太可爱啦！不过其实在画的过程中我遇到了一个问题，CB的耳机……到底是戴在哪个耳朵上呢？原版：av78977080编曲（配乐）：Crazy Bucket人声（配音）：Warma/谢拉曲绘：四迹/Warma动画：四迹/Crazy Bucket剧本：Mokurei-木灵君音频后期：DMYoung/纳兰寻风/Crazy Bucket包装：破晓天
        @SerialName("type")
        val type: Int = 0 // 1
    )

    @Serializable
    data class Dimension(
        @SerialName("height")
        val height: Int = 0, // 1080
        @SerialName("rotate")
        val rotate: Int = 0, // 0
        @SerialName("width")
        val width: Int = 0 // 1920
    )

    @Serializable
    data class HonorReply(
        @SerialName("honor")
        val honor: List<Honor> = listOf()
    ) {
        @Serializable
        data class Honor(
            @SerialName("aid")
            val aid: Int = 0, // 85440373
            @SerialName("desc")
            val desc: String = "", // 第45期每周必看
            @SerialName("type")
            val type: Int = 0, // 2
            @SerialName("weekly_recommend_num")
            val weeklyRecommendNum: Int = 0 // 45
        )
    }

    @Serializable
    data class Owner(
        @SerialName("face")
        val face: String = "", // http://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg
        @SerialName("mid")
        val mid: Long = 0, // 66606350
        @SerialName("name")
        val name: String = "" // Crazy_Bucket_陈楒潼
    )

    @Serializable
    data class Page(
        @SerialName("cid")
        val cid: Long = 0, // 146044693
        @SerialName("dimension")
        val dimension: Dimension = Dimension(),
        @SerialName("duration")
        val duration: Int = 0, // 486
        @SerialName("from")
        val from: String = "", // vupload
        @SerialName("page")
        val page: Int = 0, // 1
        @SerialName("part")
        val part: String = "", // 建议改成：建议改成：诸 神 的 电 音 节（不是）
        @SerialName("vid")
        val vid: String = "",
        @SerialName("weblink")
        val weblink: String = ""
    ) {
        @Serializable
        data class Dimension(
            @SerialName("height")
            val height: Int = 0, // 1080
            @SerialName("rotate")
            val rotate: Int = 0, // 0
            @SerialName("width")
            val width: Int = 0 // 1920
        )
    }

    @Serializable
    data class Rights(
        @SerialName("arc_pay")
        val arcPay: Int = 0, // 0
        @SerialName("autoplay")
        val autoplay: Int = 0, // 1
        @SerialName("bp")
        val bp: Int = 0, // 0
        @SerialName("clean_mode")
        val cleanMode: Int = 0, // 0
        @SerialName("download")
        val download: Int = 0, // 1
        @SerialName("elec")
        val elec: Int = 0, // 0
        @SerialName("free_watch")
        val freeWatch: Int = 0, // 0
        @SerialName("hd5")
        val hd5: Int = 0, // 1
        @SerialName("is_360")
        val is360: Int = 0, // 0
        @SerialName("is_cooperation")
        val isCooperation: Int = 0, // 1
        @SerialName("is_stein_gate")
        val isSteinGate: Int = 0, // 0
        @SerialName("movie")
        val movie: Int = 0, // 0
        @SerialName("no_background")
        val noBackground: Int = 0, // 0
        @SerialName("no_reprint")
        val noReprint: Int = 0, // 1
        @SerialName("no_share")
        val noShare: Int = 0, // 0
        @SerialName("pay")
        val pay: Int = 0, // 0
        @SerialName("ugc_pay")
        val ugcPay: Int = 0, // 0
        @SerialName("ugc_pay_preview")
        val ugcPayPreview: Int = 0 // 0
    )

    @Serializable
    data class Staff(
        @SerialName("face")
        val face: String = "", // http://i2.hdslb.com/bfs/face/c9af3b32cf74baec5a4b65af8ca18ae5ff571f77.jpg
        @SerialName("follower")
        val follower: Int = 0, // 646111
        @SerialName("label_style")
        val labelStyle: Int = 0, // 0
        @SerialName("mid")
        val mid: Long = 0, // 66606350
        @SerialName("name")
        val name: String = "", // Crazy_Bucket_陈楒潼
        @SerialName("official")
        val official: Official = Official(),
        @SerialName("title")
        val title: String = "", // UP主
        @SerialName("vip")
        val vip: Vip = Vip()
    ) {
        @Serializable
        data class Official(
            @SerialName("desc")
            val desc: String = "",
            @SerialName("role")
            val role: Int = 0, // 1
            @SerialName("title")
            val title: String = "", // bilibili 知名音乐UP主
            @SerialName("type")
            val type: Int = 0 // 0
        )

        @Serializable
        data class Vip(
            @SerialName("avatar_subscript")
            val avatarSubscript: Int = 0, // 1
            @SerialName("avatar_subscript_url")
            val avatarSubscriptUrl: String = "",
            @SerialName("due_date")
            val dueDate: Long = 0, // 1674403200000
            @SerialName("label")
            val label: Label = Label(),
            @SerialName("nickname_color")
            val nicknameColor: String = "", // #FB7299
            @SerialName("role")
            val role: Int = 0, // 3
            @SerialName("status")
            val status: Int = 0, // 1
            @SerialName("theme_type")
            val themeType: Int = 0, // 0
            @SerialName("tv_vip_pay_type")
            val tvVipPayType: Int = 0, // 0
            @SerialName("tv_vip_status")
            val tvVipStatus: Int = 0, // 0
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
                val imgLabelUriHans: String = "",
                @SerialName("img_label_uri_hans_static")
                val imgLabelUriHansStatic: String = "", // https://i0.hdslb.com/bfs/vip/8d4f8bfc713826a5412a0a27eaaac4d6b9ede1d9.png
                @SerialName("img_label_uri_hant")
                val imgLabelUriHant: String = "",
                @SerialName("img_label_uri_hant_static")
                val imgLabelUriHantStatic: String = "", // https://i0.hdslb.com/bfs/activity-plat/static/20220614/e369244d0b14644f5e1a06431e22a4d5/VEW8fCC0hg.png
                @SerialName("label_theme")
                val labelTheme: String = "", // annual_vip
                @SerialName("path")
                val path: String = "",
                @SerialName("text")
                val text: String = "", // 年度大会员
                @SerialName("text_color")
                val textColor: String = "", // #FFFFFF
                @SerialName("use_img_label")
                val useImgLabel: Boolean = false // true
            )
        }
    }

    @Serializable
    data class Stat(
        @SerialName("aid")
        val aid: Int = 0, // 85440373
        @SerialName("argue_msg")
        val argueMsg: String = "",
        @SerialName("coin")
        val coin: Int = 0, // 71048
        @SerialName("danmaku")
        val danmaku: Int = 0, // 11839
        @SerialName("dislike")
        val dislike: Int = 0, // 0
        @SerialName("evaluation")
        val evaluation: String = "",
        @SerialName("favorite")
        val favorite: Int = 0, // 58725
        @SerialName("his_rank")
        val hisRank: Int = 0, // 55
        @SerialName("like")
        val like: Int = 0, // 155954
        @SerialName("now_rank")
        val nowRank: Int = 0, // 0
        @SerialName("reply")
        val reply: Int = 0, // 2619
        @SerialName("share")
        val share: Int = 0, // 9398
        @SerialName("view")
        val view: Int = 0 // 2270927
    )

    @Serializable
    data class Subtitle(
        @SerialName("allow_submit")
        val allowSubmit: Boolean = false, // false
        @SerialName("list")
        val list: List<SubTitles> = listOf()
    )

    @Serializable
    data class SubTitles(
        @SerialName("ai_status")
        val aiStatus: Int = 0, // 2
        @SerialName("ai_type")
        val aiType: Int = 0, // 0
        @SerialName("author")
        val author: Author = Author(),
        @SerialName("id")
        val id: Long = 0, // 1315737998968715264
        @SerialName("id_str")
        val idStr: String = "", // 1315737998968715264
        @SerialName("is_lock")
        val isLock: Boolean = false, // false
        @SerialName("lan")
        val lan: String = "", // ai-zh
        @SerialName("lan_doc")
        val lanDoc: String = "", // 中文（自动生成）
        @SerialName("subtitle_url")
        val subtitleUrl: String = "",
        @SerialName("type")
        val type: Int = 0 // 1
    ) {
        @Serializable
        data class Author(
            @SerialName("birthday")
            val birthday: Int = 0, // 0
            @SerialName("face")
            val face: String = "",
            @SerialName("in_reg_audit")
            val inRegAudit: Int = 0, // 0
            @SerialName("is_deleted")
            val isDeleted: Int = 0, // 0
            @SerialName("is_fake_account")
            val isFakeAccount: Int = 0, // 0
            @SerialName("is_senior_member")
            val isSeniorMember: Int = 0, // 0
            @SerialName("mid")
            val mid: Int = 0, // 0
            @SerialName("name")
            val name: String = "",
            @SerialName("rank")
            val rank: Int = 0, // 0
            @SerialName("sex")
            val sex: String = "",
            @SerialName("sign")
            val sign: String = ""
        )
    }

    @Serializable
    data class UserGarb(
        @SerialName("url_image_ani_cut")
        val urlImageAniCut: String = ""
    )
}
