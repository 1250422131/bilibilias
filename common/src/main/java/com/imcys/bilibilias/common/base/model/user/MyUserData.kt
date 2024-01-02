package com.imcys.bilibilias.common.base.model.user

import kotlinx.serialization.Serializable


/**
 * imcys
 *
 * @create: 2022-10-30 08:12
 * @Description: 我的个人信息类
 */
@Serializable
class MyUserData  {
    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"mid":293793435,"uname":"社会易姐QwQ","userid":"bili_84675323391","sign":"高中技术宅一枚，爱好MC&电子&8-bit音乐&数码&编程，资深猿厨，粉丝群：1136462265","birthday":"2002-03-05","sex":"男","nick_free":false,"rank":"正式会员"}
     */
    var code = 0
    var message: String = ""
    var ttl = 0
    var data: DataBean = DataBean()

    override fun toString(): String {
        return "MyUserData{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", ttl=" + ttl +
                ", data=" + data +
                '}'
    }
    @Serializable
    class DataBean{
        var mid: Long = 0
        var uname: String = ""
        var userid: String = ""
        var sign: String = ""
        var birthday: String = ""
        var sex: String = ""
        var isNick_free = false
        var rank: String = ""
    }
}

