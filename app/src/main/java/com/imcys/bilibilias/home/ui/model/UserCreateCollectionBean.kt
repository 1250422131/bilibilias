package com.imcys.bilibilias.home.ui.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![获取指定用户创建的所有收藏夹信息](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/fav/info.md#%E8%8E%B7%E5%8F%96%E6%8C%87%E5%AE%9A%E7%94%A8%E6%88%B7%E5%88%9B%E5%BB%BA%E7%9A%84%E6%89%80%E6%9C%89%E6%94%B6%E8%97%8F%E5%A4%B9%E4%BF%A1%E6%81%AF)
 * ```
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": {
 *         "count": 2,
 *         "list": [
 *             {
 *                 "id": 939227072,
 *                 "fid": 9392270,
 *                 "mid": 509372,
 *                 "attr": 54,
 *                 "title": "学习",
 *                 "fav_state": 0,
 *                 "media_count": 22
 *             },
 *             {
 *                 "id": 75020272,
 *                 "fid": 750202,
 *                 "mid": 509372,
 *                 "attr": 22,
 *                 "title": "MAD/AMV",
 *                 "fav_state": 0,
 *                 "media_count": 16
 *             }
 *         ],
 *         "season": null
 *     }
 * }
 * ```
 */
@Serializable
data class UserCreateCollectionBean(
    @SerialName("count")
    val count: Int = 0, // 2
    @SerialName("list")
    val list: List<Collection> = listOf(),
) {
    @Serializable
    data class Collection(
        @SerialName("attr")
        val attr: Int = 0, // 54
        @SerialName("fav_state")
        val favState: Int = 0, // 0
        @SerialName("fid")
        val fid: Int = 0, // 9392270
        @SerialName("id")
        val id: Int = 0, // 939227072
        @SerialName("media_count")
        val mediaCount: Int = 0, // 22
        @SerialName("mid")
        val mid: Int = 0, // 509372
        @SerialName("title")
        val title: String = "" // 学习
    )
}
