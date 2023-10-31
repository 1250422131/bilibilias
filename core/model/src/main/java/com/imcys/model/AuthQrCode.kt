package com.imcys.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author:imcys
 * @create: 2022-10-26 17:25
 * @Description: 登陆二维码数据
 * @see <a href="https://github.com/SocialSisterYi/bilibili-API-collect/blob/master/docs/login/login_action/QR.md">
 *     二维码登录</a>
 * @sample
 * {
 *     "code":0,
 *     "message":"0",
 *     "ttl":1,
 *     "data":{
 *         "url":"https://passport.bilibili.com/h5-app/passport/login/scan?navhide=1\u0026qrcode_key=8587cf8106a0b863c46d6bab913537f6\u0026from=",
 *         "qrcode_key":"8587cf8106a0b863c46d6bab913537f6"
 *     }
 * }
 */
@Serializable
data class AuthQrCode(
    @SerialName("qrcode_key")
    val qrcodeKey: String,
    @SerialName("url")
    val url: String
)
