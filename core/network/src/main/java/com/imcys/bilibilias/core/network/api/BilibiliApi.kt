package com.imcys.bilibilias.core.network.api

internal const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"
internal const val BILIBILI_URL = "https://www.bilibili.com"

internal object BilibiliApi {
    const val API_BILIBILI = "https://api.bilibili.com/"
    const val PASSPORT_BILIBILI = "https://passport.bilibili.com/"
    const val WEB_QRCODE_GENERATE = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/generate"
    const val WEB_QRCODE_POLL = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/poll"
    const val EXIT = PASSPORT_BILIBILI + "login/exit/v2"
    const val DM_REAL_TIME = "x/v1/dm/list.so"

    const val FAVOURED_FOLDER_ALL = "/x/v3/fav/folder/created/list-all"
}
