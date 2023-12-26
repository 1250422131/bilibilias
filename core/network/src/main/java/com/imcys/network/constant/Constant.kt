package com.imcys.network.constant

import io.ktor.http.HttpHeaders

internal const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"
const val BILIBILI_WEB_URL = "https://www.bilibili.com"

internal const val API_BILIBILI = "https://api.bilibili.com/"

internal const val HOST_BILIBILI = "api.bilibili.com"

internal val REFERER = HttpHeaders.Referrer
internal val USER_AGENT = HttpHeaders.UserAgent

internal const val PS = 30
internal const val PAGE_SIZE = 30
