package com.imcys.bilibilias.base.utils

import androidx.collection.mutableScatterMapOf
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.home.ui.model.UserNavDataModel
import io.ktor.http.encodeURLParameter
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.TreeMap

object TokenUtils {
    var key: String? = null
        private set

    // 生成key的方法
    fun setKey(userNavDataModel: UserNavDataModel) {
        // 提取 imgKey 和 subKey
        val imgKey = userNavDataModel.data.wbiImg.imgUrl.replace(".png", "").split('/').last()
        val subKey = userNavDataModel.data.wbiImg.subUrl.replace(".png", "").split('/').last()
        val mixKey = imgKey + subKey

        // 确保 mixKey 长度足够，否则 key 生成可能失败
        key = if (mixKey.length >= (array.maxOrNull() ?: 0)) {
            array.map { mixKey[it] }.take(32).joinToString("")
        } else {
            null // 如果 mixKey 长度不足，返回null，后续逻辑可以判断是否需要重新设置key
        }
    }

    // 固定的字符索引数组
    private val array = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45,
        35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38,
        41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60,
        51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36,
        20, 34, 44, 52
    )

    // MD5 加密方法
    private fun md5(string: String): String {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            val hash = md5.digest(string.toByteArray(charset("UTF-8")))
            val sb = StringBuilder(2 * hash.size)
            for (b in hash) {
                sb.append(String.format("%02x", b.toInt() and 0xff))
            }
            sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    // 生成加密后的参数
    suspend fun NetworkService.encWbi(params: Map<String, String>): Map<String, String> {
        checkToken()
        // 初始化参数并加入时间戳
        val parameters = mutableMapOf<String, String>().apply {
            put("wts", (System.currentTimeMillis() / 1000).toString())
            putAll(params)
        }

        // 如果 key 为空则抛出异常
        val secretKey = key ?: throw IllegalStateException("Key is not set. Call setKey() first.")

        // 对参数进行排序，拼接字符串并加上 secretKey
        val sortedParams = TreeMap(parameters)
        val dataStr = sortedParams.entries.joinToString("&") { (k, v) ->
            URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8")
        } + secretKey

        // 生成签名并加入到参数中
        parameters["w_rid"] = md5(dataStr)

        return parameters
    }
}
