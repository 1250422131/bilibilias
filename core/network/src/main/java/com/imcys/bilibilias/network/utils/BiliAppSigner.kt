package com.imcys.bilibilias.network.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TreeMap
import kotlin.random.Random


object BiliAppSigner {
    const val APP_KEY: String = "4409e2ce8ffd12b8"
    const val APP_SEC: String = "59b43e04ad6965f34319062b478f83dd"

    private val CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_0123456789".toCharArray()

    fun appSign(params: MutableMap<String, String>): String? {
        // 为请求参数进行 APP 签名
        params.put("appkey", APP_KEY)
        // 按照 key 重排参数
        val sortedParams: MutableMap<String, String> = TreeMap<String, String>(params)
        // 序列化参数
        val queryBuilder = StringBuilder()
        for (entry in sortedParams.entries) {
            if (queryBuilder.isNotEmpty()) {
                queryBuilder.append('&')
            }

            try {
                queryBuilder
                    .append(URLEncoder.encode(entry.key, "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(entry.value, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                return null
            }
        }
        return generateMD5(queryBuilder.append(APP_SEC).toString())
    }

    private fun generateMD5(input: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val digest = md.digest(input.toByteArray())
            val sb = StringBuilder()
            for (b in digest) {
                sb.append(String.format("%02x", b))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    private fun randomString(len: Int, rnd: Random = Random.Default): String =
        buildString(len) { repeat(len) { append(CHARS[rnd.nextInt(CHARS.size)]) } }


    val biliTvDeviceInfo by lazy {
        val now = Date()
        val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA)
        val deviceId = randomString(20)
        val buvid = randomString(37)
        val fingerprint = sdf.format(now) + randomString(45)

        val p = mutableMapOf<String, String>()
        p["bili_local_id"] = deviceId
        p["build"] = "102801"
        p["buvid"] = buvid
        p["channel"] = "master"
        p["device"] = "OnePlus"
        p["device_id"] = deviceId
        p["device_name"] = "OnePlus7TPro"
        p["device_platform"] = "Android10OnePlusHD1910"
        p["fingerprint"] = fingerprint
        p["guid"] = buvid
        p["local_fingerprint"] = fingerprint
        p["local_id"] = buvid
        p["mobi_app"] = "android_tv_yst"
        p["networkstate"] = "wifi"
        p["platform"] = "android"
        p["sys_ver"] = "29"
        p
    }
}