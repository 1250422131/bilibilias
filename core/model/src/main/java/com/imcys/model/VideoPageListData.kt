package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![查询视频分P列表 (avid/bvid转cid)](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/video/info.md#%E6%9F%A5%E8%AF%A2%E8%A7%86%E9%A2%91%E5%88%86p%E5%88%97%E8%A1%A8-avidbvid%E8%BD%ACcid)
 * ```
 * {
 *     "code": 0,
 *     "message": "0",
 *     "ttl": 1,
 *     "data": [{
 *         "cid": 66445301,
 *         "page": 1,
 *         "from": "vupload",
 *         "part": "00. 宣传短片",
 *         "duration": 33,
 *         "vid": "",
 *         "weblink": "",
 *         "dimension": {
 *             "width": 1920,
 *             "height": 1080,
 *             "rotate": 0
 *         }
 *     }, {
 *         "cid": 35039663,
 *         "page": 2,
 *         "from": "vupload",
 *         "part": "01. 火柴人与动画师",
 *         "duration": 133,
 *         "vid": "",
 *         "weblink": "",
 *         "dimension": {
 *             "width": 1484,
 *             "height": 1080,
 *             "rotate": 0
 *         }
 *     }, {
 *         "cid": 35039678,
 *         "page": 3,
 *         "from": "vupload",
 *         "part": "02. 火柴人与动画师 II",
 *         "duration": 210,
 *         "vid": "",
 *         "weblink": "",
 *         "dimension": {
 *             "width": 1484,
 *             "height": 1080,
 *             "rotate": 0
 *         }
 *     }, {
 *         "cid": 35039693,
 *         "page": 4,
 *         "from": "vupload",
 *         "part": "03. 火柴人与动画师 III",
 *         "duration": 503,
 *         "vid": "",
 *         "weblink": "",
 *         "dimension": {
 *             "width": 992,
 *             "height": 720,
 *             "rotate": 0
 *         }
 *     },
 *     ]
 * }
 * ···
 */
@Serializable
class VideoPageListData(
    @SerialName("cid")
    val cid: Long = 0,
    @SerialName("dimension")
    val dimension: Dimension = Dimension(),
    @SerialName("duration")
    val duration: Int = 0,
    @SerialName("from")
    val from: String = "",
    @SerialName("page")
    val page: Int = 0,
    @SerialName("part")
    val part: String = "",
    @SerialName("vid")
    val vid: String = "",
    @SerialName("weblink")
    val weblink: String = ""
)
