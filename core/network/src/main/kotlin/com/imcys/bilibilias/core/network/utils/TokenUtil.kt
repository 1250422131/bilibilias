package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.network.Parameter
import io.github.aakira.napier.Napier
import io.ktor.http.encodeURLParameter
import java.security.MessageDigest

object TokenUtil {

    private var token: String? = null

    fun setCacheToken(token: String) {
        Napier.d { token }
        this.token = token
    }

    fun getMixinKey(imgKey: String, subKey: String): String {
        val s = imgKey + subKey
        return buildString {
            repeat(32) {
                append(s[mixinKeyEncTab[it]])
            }
        }
    }

    fun encWbi(
        params: Map<String, Any>,
        timestamp: Long = System.currentTimeMillis(),
    ): List<Parameter> {
        return encWbi(params, token!!, timestamp)
    }

    fun encWbi(params: Map<String, Any>, mixinKey: String, timestamp: Long): List<Parameter> {
        val sortedMap = params.toSortedMap()
        val s = sortedMap.toSortedMap().let {
            it["wts"] to timestamp / 1000
            it.entries.joinToString("&") { (k, v) ->
                "${k.encodeURLParameter()}=${v.toString().encodeURLParameter()}"
            }
        }
        return sortedMap.map { (k, v) ->
            Parameter(k.encodeURLParameter(), v.toString().encodeURLParameter())
        } + Parameter("w_rid", (s + mixinKey).toMD5().encodeURLParameter())
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun String.toMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray())
        return digest.toHexString()
    }

    private val mixinKeyEncTab = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52,
    )
}