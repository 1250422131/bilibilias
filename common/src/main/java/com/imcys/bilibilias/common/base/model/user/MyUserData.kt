package com.imcys.bilibilias.common.base.model.user

import kotlinx.serialization.Serializable

/**
 * imcys
 *
 * @create: 2022-10-30 08:12
 * @Description: 我的个人信息类
 * @see <a href="https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/member_center.md">
 *     个人中心-获取我的信息</a>
 * @sample
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
 */
@Serializable
data class MyUserData(
    var code: Int = 0,
    var message: String = "",
    var ttl: Int = 0,
    var data: DataBean = DataBean()
) {
    @Serializable
    data class DataBean(
        var mid: Long = 0,
        var uname: String = "",
        var userid: String? = null,
        var sign: String? = null,
        var birthday: String? = null,
        var sex: String? = null,
        var isNick_free: Boolean = false,
        var rank: String? = null,
    )
}
