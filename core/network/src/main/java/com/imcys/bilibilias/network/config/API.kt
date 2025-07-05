package com.imcys.bilibilias.network.config

object API {

    object BILIBILI {
        const val WEB_API_BASE_URL = "https://api.bilibili.com/"

        const val APP_API_BASE_URL = "https://app.bilibili.com/"

        const val LOGIN_BASE_URL = "https://passport.bilibili.com/"

        const val SPACE_BASE_URL = "https://space.bilibili.com/"

        const val WEB_QRCODE_GENERATE_URL = LOGIN_BASE_URL + "x/passport-login/web/qrcode/generate"

        const val TV_QRCODE_GENERATE_URL = LOGIN_BASE_URL + "x/passport-tv-login/qrcode/auth_code"

        // 检测扫码登录状态
        const val WEB_QRCODE_POLL_URL = LOGIN_BASE_URL + "x/passport-login/web/qrcode/poll"

        const val TV_QRCODE_POLL_URL = LOGIN_BASE_URL + "x/passport-tv-login/qrcode/poll"


        const val WEB_LOGIN_INFO_URL = WEB_API_BASE_URL + "x/web-interface/nav"

        const val TV_LOGIN_INFO_URL = APP_API_BASE_URL + "x/v2/account/myinfo"

    }
}