package com.imcys.bilibilias.base.api


class BilibiliApi() {

    /**
     * 常见注解
     * mid为UID
     *
     */

    val serviceApi: String = "https://api.bilibili.com/"
    val loginApi: String = "http://passport.bilibili.com/"


    val getLoginQRPath: String = loginApi + "x/passport-login/web/qrcode/generate"

    //需要登陆密钥
    val getLoginStatePath: String = loginApi + "x/passport-login/web/qrcode/poll"

    //需要传入PS为展示项
    val homeRCMDVideoPath: String = serviceApi + "x/web-interface/index/top/feed/rcmd"

    //需要传入mid
    val getUserInfoPath: String = serviceApi + "x/space/acc/info"

    //获取个人基本信息
    val getMyUserData = serviceApi + "x/member/web/account"

    //post aid bvid like:1,2 csrf:j_xx
    val likeVideoPath = serviceApi + "x/web-interface/archive/like"




}
