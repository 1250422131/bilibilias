package com.imcys.bilibilias.network.utils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.TreeMap


object BiliAppSigner {
    const val APP_KEY: String = "27eb53fc9058f8c3"
    const val APP_SEC: String = "c2ed53a74eeefe3cf99fbd01d8c9c375"

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
}