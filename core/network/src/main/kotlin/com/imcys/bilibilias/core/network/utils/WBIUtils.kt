package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.network.Parameter
import io.ktor.http.encodeURLParameter
import org.apache.commons.codec.digest.DigestUtils
import java.util.StringJoiner

/**
 * 签名算法
 */
object WBIUtils {
    private val MIXIN_KEY_ENC_TAB = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52,
    )

    fun getMixinKey(imgKey: String, subKey: String): String {
        val mixKey = imgKey + subKey
        return MIXIN_KEY_ENC_TAB.map {
            mixKey[it]
        }.take(32).joinToString("")
    }

    fun encWbi(params: List<Parameter>, mixinKey: String): List<Parameter> {
        val parameters = sortedSetOf<Parameter>(compareBy { it.name }).apply {
            add(Parameter("wts", ((System.currentTimeMillis() / 1000).toString())))
            addAll(params)
        }

        val param = parameters.joinToString("&") { (k, v) ->
            k.encodeURLParameter() + "=" + v.encodeURLParameter()
        } + mixinKey
        parameters += Parameter("w_rid", DigestUtils.md5Hex(param))
        return parameters.map { Parameter(it.name, it.value) }
    }

    fun encWbi2(params: List<Parameter>, mixinKey: String): List<Parameter> {
        val map = sortedMapOf<String, String>(
            "wts" to (System.currentTimeMillis() / 1000).toString(),
        ).apply {
            params.forEach { put(it.name, it.value) }
        }

        val param = StringJoiner("&")
        map.forEach { entry ->
            param.add(
                entry.key + "=" + entry.value.encodeURLParameter(),
            )
        }
        return map.map { Parameter(it.key, it.value) } +
            Parameter(
                "w_rid",
                DigestUtils.md5Hex(param.toString() + mixinKey),
            )
    }
}
