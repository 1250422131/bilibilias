package com.imcys.bilibilias.base.utils

import androidx.collection.mutableScatterMapOf
import com.imcys.bilibilias.home.ui.model.UserNavDataModel
import io.ktor.http.encodeURLParameter
import java.security.MessageDigest

object TokenUtils {
    private var key: String? = null
    fun setKey(userNavDataModel: UserNavDataModel) {
        val imgKey = userNavDataModel.data.wbiImg.imgUrl.replace(".png", "").split('/').last()
        val subKey = userNavDataModel.data.wbiImg.subUrl.replace(".png", "").split('/').last()
        val mixKey = imgKey + subKey

        key = array.map { mixKey[it] }.take(32).joinToString("")
    }

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

    fun encWbi(params: Map<String, String>): Map<String, String> {
        val parameters = mutableScatterMapOf<String, String>()
            .apply {
                put("wts", (System.currentTimeMillis() / 1000).toString())
                putAll(params)
            }

        val param = parameters.joinToString("&") { k, v ->
            k.encodeURLParameter() + "=" + v.encodeURLParameter()
        } + key
        parameters.put("w_rid", md5(param))
        return parameters.asMutableMap()
    }
}
