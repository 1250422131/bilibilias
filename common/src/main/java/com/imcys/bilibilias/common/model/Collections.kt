package com.imcys.bilibilias.common.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![获取收藏夹内容明细列表](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/fav/list.md#%E8%8E%B7%E5%8F%96%E6%94%B6%E8%97%8F%E5%A4%B9%E5%86%85%E5%AE%B9%E6%98%8E%E7%BB%86%E5%88%97%E8%A1%A8)
 *
 * ```
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": {
 *         "info": {
 *             "id": 1052622027,
 *             "fid": 10526220,
 *             "mid": 686127,
 *             "attr": 54,
 *             "title": "猛 男 生 存",
 *             "cover": "http://i2.hdslb.com/bfs/archive/bb51ee8a5fc5e03996138155f0f682d30ee16484.jpg",
 *             "upper": {
 *                 "mid": 686127,
 *                 "name": "籽岷",
 *                 "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp",
 *                 "followed": true,
 *                 "vip_type": 2,
 *                 "vip_statue": 1
 *             },
 *             "cover_type": 2,
 *             "cnt_info": {
 *                 "collect": 3393,
 *                 "play": 184768,
 *                 "thumb_up": 3916,
 *                 "share": 44
 *             },
 *             "type": 11,
 *             "intro": "猛 男 生 存",
 *             "ctime": 1598884758,
 *             "mtime": 1598884758,
 *             "state": 0,
 *             "fav_state": 0,
 *             "like_state": 0,
 *             "media_count": 28
 *         },
 *         "medias": [
 *             {
 *                 "id": 371494037,
 *                 "type": 2,
 *                 "title": "猛 男 生 存",
 *                 "cover": "http://i2.hdslb.com/bfs/archive/bb51ee8a5fc5e03996138155f0f682d30ee16484.jpg",
 *                 "intro": "如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~\n也可以把我的视频分享给你们的朋友们~\n\n第一集：BV1CZ4y1T7gC\n第二集：BV1oA411a72k\n第三集：BV1fK4y1e7Yj\n第四集：BV1Ya4y1E7Y6\n第五集：BV17V411z75A\n第六集：BV1oi4y137sw\n第七集：BV1Wt4y1D7Uu\n第八集：BV1Bp4y1q7y9\n第九集：BV1Lv411v7G2\n第十集：BV1Xi4y137ER\n第十一集：BV1nC4y1879J\n第十二集：BV1K54y1",
 *                 "page": 1,
 *                 "duration": 546,
 *                 "upper": {
 *                     "mid": 686127,
 *                     "name": "籽岷",
 *                     "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp"
 *                 },
 *                 "attr": 0,
 *                 "cnt_info": {
 *                     "collect": 11256,
 *                     "play": 1638040,
 *                     "danmaku": 7697
 *                 },
 *                 "link": "bilibili://video/371494037",
 *                 "ctime": 1595690513,
 *                 "pubtime": 1595690513,
 *                 "fav_time": 1598884777,
 *                 "bv_id": "BV1CZ4y1T7gC",
 *                 "bvid": "BV1CZ4y1T7gC",
 *                 "season": null
 *             },
 *             {
 *                 "id": 328991940,
 *                 "type": 2,
 *                 "title": "猛 男 生 存 2",
 *                 "cover": "http://i1.hdslb.com/bfs/archive/aa801612ea0229a08d000a525b715af24cba0964.jpg",
 *                 "intro": "如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~\n也可以把我的视频分享给你们的朋友们~\n\n第一集：BV1CZ4y1T7gC\n第二集：BV1oA411a72k\n第三集：BV1fK4y1e7Yj\n第四集：BV1Ya4y1E7Y6\n第五集：BV17V411z75A\n第六集：BV1oi4y137sw\n第七集：BV1Wt4y1D7Uu\n第八集：BV1Bp4y1q7y9\n第九集：BV1Lv411v7G2\n第十集：BV1Xi4y137ER\n第十一集：BV1nC4y1879J\n第十二集：BV1K54y1",
 *                 "page": 1,
 *                 "duration": 644,
 *                 "upper": {
 *                     "mid": 686127,
 *                     "name": "籽岷",
 *                     "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp"
 *                 },
 *                 "attr": 0,
 *                 "cnt_info": {
 *                     "collect": 8695,
 *                     "play": 1334651,
 *                     "danmaku": 6064
 *                 },
 *                 "link": "bilibili://video/328991940",
 *                 "ctime": 1595770876,
 *                 "pubtime": 1595770876,
 *                 "fav_time": 1598884783,
 *                 "bv_id": "BV1oA411a72k",
 *                 "bvid": "BV1oA411a72k",
 *                 "season": null
 *             },
 *             {
 *                 "id": 884042215,
 *                 "type": 2,
 *                 "title": "猛 男 生 存 3",
 *                 "cover": "http://i1.hdslb.com/bfs/archive/f99059637c110dcd1cdae765a946801fbcefe4ab.jpg",
 *                 "intro": "如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~\n也可以把我的视频分享给你们的朋友们~\n\n第一集：BV1CZ4y1T7gC\n第二集：BV1oA411a72k\n第三集：BV1fK4y1e7Yj\n第四集：BV1Ya4y1E7Y6\n第五集：BV17V411z75A\n第六集：BV1oi4y137sw\n第七集：BV1Wt4y1D7Uu\n第八集：BV1Bp4y1q7y9\n第九集：BV1Lv411v7G2\n第十集：BV1Xi4y137ER\n第十一集：BV1nC4y1879J\n第十二集：BV1K54y1",
 *                 "page": 1,
 *                 "duration": 703,
 *                 "upper": {
 *                     "mid": 686127,
 *                     "name": "籽岷",
 *                     "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp"
 *                 },
 *                 "attr": 0,
 *                 "cnt_info": {
 *                     "collect": 9449,
 *                     "play": 1429408,
 *                     "danmaku": 8243
 *                 },
 *                 "link": "bilibili://video/884042215",
 *                 "ctime": 1595847079,
 *                 "pubtime": 1595847079,
 *                 "fav_time": 1598884788,
 *                 "bv_id": "BV1fK4y1e7Yj",
 *                 "bvid": "BV1fK4y1e7Yj",
 *                 "season": null
 *             },
 *             {
 *                 "id": 669013980,
 *                 "type": 2,
 *                 "title": "猛 男 生 存 4",
 *                 "cover": "http://i1.hdslb.com/bfs/archive/def0f7009cb9a8b581ee03be9565918ff0c1913d.jpg",
 *                 "intro": "如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~\n也可以把我的视频分享给你们的朋友们~\n\n第一集：BV1CZ4y1T7gC\n第二集：BV1oA411a72k\n第三集：BV1fK4y1e7Yj\n第四集：BV1Ya4y1E7Y6\n第五集：BV17V411z75A\n第六集：BV1oi4y137sw\n第七集：BV1Wt4y1D7Uu\n第八集：BV1Bp4y1q7y9\n第九集：BV1Lv411v7G2\n第十集：BV1Xi4y137ER\n第十一集：BV1nC4y1879J\n第十二集：BV1K54y1",
 *                 "page": 1,
 *                 "duration": 895,
 *                 "upper": {
 *                     "mid": 686127,
 *                     "name": "籽岷",
 *                     "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp"
 *                 },
 *                 "attr": 0,
 *                 "cnt_info": {
 *                     "collect": 9950,
 *                     "play": 1309544,
 *                     "danmaku": 13551
 *                 },
 *                 "link": "bilibili://video/669013980",
 *                 "ctime": 1595943988,
 *                 "pubtime": 1595943988,
 *                 "fav_time": 1598884792,
 *                 "bv_id": "BV1Ya4y1E7Y6",
 *                 "bvid": "BV1Ya4y1E7Y6",
 *                 "season": null
 *             },
 *             {
 *                 "id": 414034824,
 *                 "type": 2,
 *                 "title": "猛 男 生 存 5",
 *                 "cover": "http://i2.hdslb.com/bfs/archive/b4844ac89dde221d13bb8ddff80a8c4658bf7dc5.jpg",
 *                 "intro": "如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~\n也可以把我的视频分享给你们的朋友们~\n\n第一集：BV1CZ4y1T7gC\n第二集：BV1oA411a72k\n第三集：BV1fK4y1e7Yj\n第四集：BV1Ya4y1E7Y6\n第五集：BV17V411z75A\n第六集：BV1oi4y137sw\n第七集：BV1Wt4y1D7Uu\n第八集：BV1Bp4y1q7y9\n第九集：BV1Lv411v7G2\n第十集：BV1Xi4y137ER\n第十一集：BV1nC4y1879J\n第十二集：BV1K54y1",
 *                 "page": 1,
 *                 "duration": 814,
 *                 "upper": {
 *                     "mid": 686127,
 *                     "name": "籽岷",
 *                     "face": "http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp"
 *                 },
 *                 "attr": 0,
 *                 "cnt_info": {
 *                     "collect": 9446,
 *                     "play": 1235998,
 *                     "danmaku": 9021
 *                 },
 *                 "link": "bilibili://video/414034824",
 *                 "ctime": 1596023668,
 *                 "pubtime": 1596023668,
 *                 "fav_time": 1598884798,
 *                 "bv_id": "BV17V411z75A",
 *                 "bvid": "BV17V411z75A",
 *                 "season": null
 *             }
 *         ],
 *         "has_more": true
 *     }
 * }
 * ```
 */
@Serializable
data class Collections(
    @SerialName("has_more")
    val hasMore: Boolean = false, // true
    @SerialName("info")
    val info: Info = Info(),
    @SerialName("medias")
    val medias: List<Media> = listOf()
) {
    @Serializable
    data class Info(
        @SerialName("attr")
        val attr: Int = 0, // 54
        @SerialName("cnt_info")
        val cntInfo: CntInfo = CntInfo(),
        @SerialName("cover")
        val cover: String = "", // http://i2.hdslb.com/bfs/archive/bb51ee8a5fc5e03996138155f0f682d30ee16484.jpg
        @SerialName("cover_type")
        val coverType: Int = 0, // 2
        @SerialName("ctime")
        val ctime: Int = 0, // 1598884758
        @SerialName("fav_state")
        val favState: Int = 0, // 0
        @SerialName("fid")
        val fid: Int = 0, // 10526220
        @SerialName("id")
        val id: Int = 0, // 1052622027
        @SerialName("intro")
        val intro: String = "", // 猛 男 生 存
        @SerialName("like_state")
        val likeState: Int = 0, // 0
        @SerialName("media_count")
        val mediaCount: Int = 0, // 28
        @SerialName("mid")
        val mid: Int = 0, // 686127
        @SerialName("mtime")
        val mtime: Int = 0, // 1598884758
        @SerialName("state")
        val state: Int = 0, // 0
        @SerialName("title")
        val title: String = "", // 猛 男 生 存
        @SerialName("type")
        val type: Int = 0, // 11
        @SerialName("upper")
        val upper: Upper = Upper()
    ) {
        @Serializable
        data class CntInfo(
            @SerialName("collect")
            val collect: Int = 0, // 3393
            @SerialName("play")
            val play: Int = 0, // 184768
            @SerialName("share")
            val share: Int = 0, // 44
            @SerialName("thumb_up")
            val thumbUp: Int = 0 // 3916
        )

        @Serializable
        data class Upper(
            @SerialName("face")
            val face: String = "", // http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp
            @SerialName("followed")
            val followed: Boolean = false, // true
            @SerialName("mid")
            val mid: Int = 0, // 686127
            @SerialName("name")
            val name: String = "", // 籽岷
            @SerialName("vip_statue")
            val vipStatue: Int = 0, // 1
            @SerialName("vip_type")
            val vipType: Int = 0 // 2
        )
    }

    @Serializable
    data class Media(
        @SerialName("attr")
        val attr: Int = 0, // 0
        @SerialName("bv_id")
        val bvId: String = "", // BV1CZ4y1T7gC
        @SerialName("bvid")
        val bvid: String = "", // BV1CZ4y1T7gC
        @SerialName("cnt_info")
        val cntInfo: CntInfo = CntInfo(),
        @SerialName("cover")
        val cover: String = "", // http://i2.hdslb.com/bfs/archive/bb51ee8a5fc5e03996138155f0f682d30ee16484.jpg
        @SerialName("ctime")
        val ctime: Int = 0, // 1595690513
        @SerialName("duration")
        val duration: Int = 0, // 546
        @SerialName("fav_time")
        val favTime: Int = 0, // 1598884777
        @SerialName("id")
        val id: Int = 0, // 371494037
        @SerialName("intro")
        val intro: String = "", // 如果大家喜欢我的视频，别忘了点个赞，一键三连，或者关注我的频道哦~也可以把我的视频分享给你们的朋友们~第一集：BV1CZ4y1T7gC第二集：BV1oA411a72k第三集：BV1fK4y1e7Yj第四集：BV1Ya4y1E7Y6第五集：BV17V411z75A第六集：BV1oi4y137sw第七集：BV1Wt4y1D7Uu第八集：BV1Bp4y1q7y9第九集：BV1Lv411v7G2第十集：BV1Xi4y137ER第十一集：BV1nC4y1879J第十二集：BV1K54y1
        @SerialName("link")
        val link: String = "", // bilibili://video/371494037
        @SerialName("page")
        val page: Int = 0, // 1
        @SerialName("pubtime")
        val pubtime: Int = 0, // 1595690513
        @SerialName("title")
        val title: String = "", // 猛 男 生 存
        @SerialName("type")
        val type: Int = 0, // 2
        @SerialName("upper")
        val upper: Upper = Upper()
    ) {
        @Serializable
        data class CntInfo(
            @SerialName("collect")
            val collect: Int = 0, // 11256
            @SerialName("danmaku")
            val danmaku: Int = 0, // 7697
            @SerialName("play")
            val play: Int = 0 // 1638040
        )

        @Serializable
        data class Upper(
            @SerialName("face")
            val face: String = "", // http://i0.hdslb.com/bfs/face/7efb679569b2faeff38fa08f6f992fa1ada5e948.webp
            @SerialName("mid")
            val mid: Int = 0, // 686127
            @SerialName("name")
            val name: String = "" // 籽岷
        )
    }
}
