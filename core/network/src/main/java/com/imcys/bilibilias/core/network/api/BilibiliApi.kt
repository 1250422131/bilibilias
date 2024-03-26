package com.imcys.bilibilias.core.network.api
internal const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"
internal object BilibiliApi {
    const val API_BILIBILI = "https://api.bilibili.com/"
    const val PASSPORT_BILIBILI = "https://passport.bilibili.com/"
    const val WEB_QRCODE_GENERATE = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/generate"
    const val WEB_QRCODE_POLL = PASSPORT_BILIBILI + "x/passport-login/web/qrcode/poll"

}