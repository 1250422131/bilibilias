package com.imcys.bilibilias.core.network.utils

import com.imcys.bilibilias.core.network.Parameter
import com.imcys.bilibilias.core.network.repository.LoginRepository
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.TreeMap
import javax.inject.Singleton

@Singleton
class TokenUtil(private val loginRepository: LoginRepository) {
    private var requestToken: String = ""
    private val array = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45,
        35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28, 14, 39, 12, 38,
        41, 13, 37, 48, 7, 16, 24, 55, 40, 61, 26, 17, 0, 1, 60,
        51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11, 36,
        20, 34, 44, 52,
    )

    suspend fun getParamStr(params: MutableMap<String, String>): List<Parameter> {
        var security = requestToken
        if (requestToken.isEmpty()) {
            val userNavDataModel = loginRepository.nav()
            val imgUrl = userNavDataModel.wbiImg.imgUrl
            val subUrl = userNavDataModel.wbiImg.subUrl

            var tempImgs = imgUrl.split('/')
            val imgVal: String = tempImgs[tempImgs.size - 1].replace(".png", "")
            tempImgs = subUrl.split('/')
            val subVal: String = tempImgs[tempImgs.size - 1].replace(".png", "")

            val preToken = imgVal + subVal
            security = getBiliMixin(preToken)
        }

        return genBiliSign(params, security)
    }


    private fun genBiliSign(params: MutableMap<String, String>, secret: String): List<Parameter> {
        val wts = System.currentTimeMillis() / 1000
        params["wts"] = wts.toString()
        val sortedParams = TreeMap(params)
        val dataStrBuilder = StringBuilder()
        for (k in sortedParams.keys) {
            dataStrBuilder.append(k).append("=").append(sortedParams[k]).append("&")
        }
        val dataStr = dataStrBuilder.substring(0, dataStrBuilder.length - 1) + secret
        params["w_rid"] = md5(dataStr)
        return params.map { (k, v) ->
            Parameter(
                URLEncoder.encode(k, "UTF-8"),
                URLEncoder.encode(v, "UTF-8"),
            )
        }
    }

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
        if (requestToken.isNotEmpty()) {
            return requestToken
        }

        val requestTokenBuilder = StringBuilder()
        for (v in array) {
            requestTokenBuilder.append(value[v])
        }
        requestToken = requestTokenBuilder.toString().substring(0, 32)
        return requestToken
    }

}