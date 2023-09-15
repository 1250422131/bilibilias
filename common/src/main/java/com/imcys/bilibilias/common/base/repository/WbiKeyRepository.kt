package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.WTS
import com.imcys.bilibilias.common.base.constant.W_RID
import com.imcys.bilibilias.common.base.model.UserNav
import com.liulishuo.okdownload.core.Util
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLParameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WbiKeyRepository @Inject constructor(private val httpClient: HttpClient) {

    private val mixinKeyEncTab = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52
    )

    private var token: String? = null

    suspend fun getUserNavToken(params: List<Pair<String, String>>): List<Pair<String, String>> {
        val userNav = httpClient.get(BilibiliApi.Token).body<UserNav>()
        return getParam(params, userNav.imgKey, userNav.subKey)
    }

    private fun getParam(
        params: List<Pair<String, String>>,
        imgKey: String,
        subKey: String
    ): List<Pair<String, String>> {
        val token = token
        if (token != null) return sign(params, token)

        val mixinKey = getMixinKey(imgKey, subKey)
        return sign(params, mixinKey)
    }

    private fun getMixinKey(imgKey: String, subKey: String): String {
        val s = imgKey + subKey
        val key = StringBuilder()
        for (i in 0..31) {
            key.append(s[mixinKeyEncTab[i]])
        }
        token = key.toString()
        return token!!
    }

    private fun sign(params: List<Pair<String, String>>, mixinKey: String): List<Pair<String, String>> {
        val map = mutableListOf(WTS to (System.currentTimeMillis() / 1000).toString()).apply {
            addAll(params)
            sortBy { it.first }
        }
        val param = map.joinToString("&") { (k, v) ->
            k + "=" + v.encodeURLParameter()
        }
        val s = param + mixinKey
        val wbiSign: String = Util.md5(s)!!
        map.add(W_RID to wbiSign)
        return map
    }
}
