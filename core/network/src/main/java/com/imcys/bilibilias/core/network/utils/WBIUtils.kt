package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.network.Parameter
import io.ktor.http.encodeURLParameter
import java.math.BigInteger
import java.security.MessageDigest

/**
 * 签名算法
 */
object WBIUtils {
    private var mixKey: String? = null
    private val MIXIN_KEY_ENC_TAB = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52
    )

    /**
     * bar=514&baz=1919810&foo=114&wts=1687541921&w_rid=26e82b1b9b3a11dbb1807a9228a40d3b
     */
    fun getMixinKey(imgKey: String, subKey: String): String {
        mixKey?.let {
            return it
        }
        val mixKey = imgKey + subKey
        this.mixKey = mixKey
        return MIXIN_KEY_ENC_TAB.map {
            mixKey[it]
        }.take(32).joinToString("")
    }

    fun encWbi(params: List<Parameter>, mixinKey: String): List<Parameter> {
        val parameters =
            mutableListOf(Parameter("wts", (System.currentTimeMillis() / 1000).toString())).apply {
                addAll(params)
                sortBy { it.name }
            }

        val param = parameters.joinToString("&") { (k, v) ->
            k + "=" + v.encodeURLParameter()
        }
        val wbiSign = md5Hash(param + mixinKey)
        parameters += Parameter("w_rid", wbiSign)
        return parameters.map { Parameter(it.name, it.value) }
    }

    private fun md5Hash(str: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
}