package com.imcys.network.utils

import com.imcys.common.utils.md5
import com.imcys.network.repository.Parameter
import io.ktor.http.encodeURLParameter

/**
 * 签名算法
 */
object SignatureUtils {
    fun signature(
        params: List<Parameter>,
        mixinKey: String
    ): List<Parameter> {
        val p = Parameter("wts", (System.currentTimeMillis() / 1000).toString())
        val parameters = mutableListOf(p).apply {
            addAll(params)
            sortBy { it.first }
        }
        val param = parameters.joinToString("&") { (k, v) ->
            k + "=" + v.encodeURLParameter()
        }
        val s = param + mixinKey
        val wbiSign = md5(s)
        parameters.add(Parameter("w_rid", wbiSign))
        return parameters.map { Parameter(it.first, it.second) }
    }
}
