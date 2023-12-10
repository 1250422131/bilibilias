package com.imcys.model

import com.imcys.model.video.Author
import com.imcys.model.video.DescV2
import com.imcys.model.video.Owner
import com.imcys.model.video.PageData
import com.imcys.model.video.Rights
import com.imcys.model.video.Staff
import com.imcys.model.video.UserGarb
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
data class VideoDetails(
    @SerialName("aid")
    val aid: LongAsString = 0L,
    @SerialName("bvid")
    val bvid: String = "",
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("ctime")
    val ctime: Int = 0,
    @SerialName("desc")
    val desc: String = "",
    @SerialName("desc_v2")
    val descV2: List<DescV2>? = null,
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("dynamic")
    val `dynamic`: String = "",
    @SerialName("owner")
    val owner: Owner = Owner(),
    @SerialName("pages")
    val pageData: List<PageData> = listOf(),
    @SerialName("pic")
    val pic: String = "",
    @SerialName("pubdate")
    val pubdate: Int = 0,
    @SerialName("redirect_url")
    val redirectUrl: String? = null,
    @SerialName("rights")
    val rights: Rights = Rights(),
    @SerialName("staff")
    val staff: List<Staff> = listOf(),
    @SerialName("stat")
    val stat: Stat = Stat(),
    @SerialName("subtitle")
    val subtitle: Subtitle = Subtitle(),
    @SerialName("title")
    val title: String = "",
    @SerialName("user_garb")
    val userGarb: UserGarb = UserGarb(),
    @SerialName("videos")
    val videos: Int = 0
) {
    @Serializable
    data class Stat(
        @SerialName("aid")
        val aid: Long = 0,
        @SerialName("coin")
        val coin: Int = 0,
        @SerialName("danmaku")
        val danmaku: Int = 0,
        @SerialName("evaluation")
        val evaluation: String = "",
        @SerialName("favorite")
        val favorite: Int = 0,
        @SerialName("like")
        val like: Int = 0,
        @SerialName("reply")
        val reply: Int = 0,
        @SerialName("share")
        val share: Int = 0,
        @SerialName("view")
        val view: Int = 0
    )

    @Serializable
    data class Subtitle(
        @SerialName("allow_submit")
        val allowSubmit: Boolean = false,
        @SerialName("list")
        val list: List<SubSubTitle> = listOf()
    )

    @Serializable
    data class SubSubTitle(
        @SerialName("id")
        val id: Long = 0,
        @SerialName("lan")
        val lan: String = "",
        @SerialName("lan_doc")
        val lanDoc: String = "",
        @SerialName("is_lock")
        val isLock: Boolean = false,
        @SerialName("author_mid")
        val authorMid: Long = 0,
        @SerialName("subtitle_url")
        val subtitleUrl: String = "",
        @SerialName("author")
        val author: Author = Author()
    )
}
