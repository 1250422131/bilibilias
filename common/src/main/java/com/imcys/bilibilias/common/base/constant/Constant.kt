package com.imcys.bilibilias.common.base.constant

import android.os.Environment
import java.io.File

const val BROWSER_USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.76"

const val USER_AGENT = "User-Agent"

const val BILIBILI_URL = "https://www.bilibili.com"

const val ROAM_API = "https://api.bilibili.com/"

const val SET_COOKIE = "Set-Cookie"

const val COOKIE = "cookie"

const val COOKIES = "cookies"

const val REFERER = "Referer"

val DOWNLOAD_DEFAULT_PATH by lazy {
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + "BILIBILIAS"
}

const val FILE_PROVIDER = "com.imcys.bilibilias.fileprovider"

val BROWSER_FINGERPRINT = mapOf(
    "web_location" to "1550101",
    "platform" to "web",
    "token" to "",
    "dm_img_list" to "[]",
    "dm_img_str" to "V2ViR0wgMS4wIChPcGVuR0wgRVMgMi4wIENocm9taXVtKQ",
    "dm_img_inter" to "{\"ds\":[],\"wh\":[3428,1836,80],\"of\":[72,144,72]}",
    "dm_cover_img_str" to "QU5HTEUgKE5WSURJQSwgTlZJRElBIEdlRm9yY2UgR1RYIDEwODAgKDB4MDAwMDFCODApIERpcmVjdDNEMTEgdnNfNV8wIHBzXzVfMCwgRDNEMTEpR29vZ2xlIEluYy4gKE5WSURJQS",
)