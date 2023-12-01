package com.imcys.network.constant

import io.ktor.http.HttpHeaders

const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/70.0.3538.77 Chrome/70.0.3538.77 Safari/537.36"
const val BILIBILI_WEB_URL = "https://www.bilibili.com"

const val API_BILIBILI = "https://api.bilibili.com/"

const val HOST_BILIBILI = "api.bilibili.com"

val REFERER = HttpHeaders.Referrer
val USER_AGENT = HttpHeaders.UserAgent

const val PS = 30
const val PAGE_SIZE = 30
