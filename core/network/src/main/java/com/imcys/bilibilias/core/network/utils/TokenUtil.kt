package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.network.Parameter
import io.ktor.http.encodeURLParameter
import java.security.MessageDigest
import java.util.StringJoiner
import java.util.TreeMap

object TokenUtil {
    private var requestToken: String? = null
    private val array = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45,
        35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38,
        41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60,
        51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36,
        20, 34, 44, 52
    )

    private fun md5(string: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val hash = md5.digest(string.toByteArray(charset("UTF-8")))
        val sb = StringBuilder(2 * hash.size)
        for (b in hash) {
            sb.append(String.format("%02x", b.toInt() and 0xff))
        }
        return sb.toString()
    }

    private fun getBiliMixin(value: String): String {
        requestToken?.let { return it }
        val requestTokenBuilder = StringBuilder()
        for (v in array) {
            requestTokenBuilder.append(value[v])
        }
        return requestTokenBuilder.toString().substring(0, 32).also { requestToken = it }
    }

    suspend fun getParamStr(
        params: MutableMap<String, String>,
        imgKey: String,
        subKey: String
    ): List<Parameter> {
        val token = requestToken ?: getBiliMixin(imgKey + subKey)
        return genBiliSign(params, token)
    }

    fun genBiliSign(params: MutableMap<String, String>, secret: String): List<Parameter> {
        params["wts"] = (System.currentTimeMillis() / 1000).toString()
        val sortedParams = TreeMap(params)
        val stringBuilder = StringJoiner("&")
        for ((k, v) in sortedParams) {
            stringBuilder.add(k + "=" + v.encodeURLParameter())
        }
        val dataStr = stringBuilder.toString() + secret
        params["w_rid"] = md5(dataStr)
        return params.map { Parameter(it.key.encodeURLParameter(), it.value.encodeURLParameter()) }
    }
}
