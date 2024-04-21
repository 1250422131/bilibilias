package com.imcys.bilibilias.core.network.api

internal const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"
internal const val BILIBILI_URL = "https://www.bilibili.com"

internal object BilibiliApi {

    const val API_BILIBILI = "https://api.bilibili.com/"
    const val API_HOST = "api.bilibili.com/"
    const val PASSPORT_BILIBILI = "https://passport.bilibili.com/"
    const val WEB_QRCODE_GENERATE = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/generate"
    const val WEB_QRCODE_POLL = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/poll"
    const val EXIT = PASSPORT_BILIBILI + "login/exit/v2"
    const val DM_REAL_TIME = "x/v1/dm/list.so"
    const val NAV_BAR = "x/web-interface/nav"
    const val FAVOURED_FOLDER_ALL = "/x/v3/fav/folder/created/list-all"

    const val VIEW = "x/web-interface/view"
    const val CARD = "x/web-interface/card"

    const val ARCHIVE_HAS_LIKE = "x/web-interface/archive/has/like"
    const val ARCHIVE_LIKE = "/x/web-interface/archive/like"
    const val ARCHIVE_COINS = "x/web-interface/archive/coins"
    const val COIN_ADD = "x/web-interface/coin/add"
    const val ARCHIVE_FAVOURED = "x/v2/fav/video/favoured"
    const val ARCHIVE_RESOURCE_FAVOURED = "/x/v3/fav/resource/deal"
}
