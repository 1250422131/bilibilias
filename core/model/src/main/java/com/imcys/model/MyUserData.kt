package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ![个人中心-获取我的信息](https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/member_center.md)
 *
 * ```
 * {
 *     "code":0,
 *     "message":0,
 *     "ttl":1,
 *     "data":{
 *         "mid":293793435,
 *         "uname":"社会易姐QwQ",
 *         "userid":"bili_84675323391",
 *         "sign":"高中技术宅一枚，爱好MC&电子&8-bit音乐&数码&编程，资深猿厨，粉丝群：1136462265",
 *         "birthday":"2002-03-05",
 *         "sex":"男",
 *         "nick_free":false,
 *         "rank":"正式会员"
 *     }
 * }
 * ```
 */
@Serializable
data class MyUserData(
    @SerialName("mid")
    val mid: Long = 0, // 293793435
    @SerialName("nick_free")
    val nickFree: Boolean = false, // false
    @SerialName("rank")
    val rank: String = "", // 正式会员
    @SerialName("sign")
    val sign: String = "", // 高中技术宅一枚，爱好MC&电子&8-bit音乐&数码&编程，资深猿厨，粉丝群：1136462265
    @SerialName("uname")
    val uname: String = "", // 社会易姐QwQ
    @SerialName("userid")
    val userid: String = "" // bili_84675323391
)
