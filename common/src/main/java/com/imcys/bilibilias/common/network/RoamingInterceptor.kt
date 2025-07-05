package com.imcys.bilibilias.common.network

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.config.TvUserInfoRepository
import com.imcys.bilibilias.common.utils.BiliAppSigner
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class RoamingInterceptor : Interceptor {
    private val whitelist: List<String> = listOf(
        BilibiliApi.getVideoDataPath,
        BilibiliApi.videoInfoV2,
        BilibiliApi.videoPlayPath,
        BilibiliApi.bangumiPlayPath,
        BilibiliApi.videoPageListPath,
        BilibiliApi.bangumiVideoDataPath,
    )

    private val retryPathSuffix: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url.toString()

        // Check if URL is in the whitelist
        if (TvUserInfoRepository.enabledRoaming && whitelist.any { requestUrl.contains(it) } && !requestUrl.contains(BilibiliApi.roamingApi)) {
            val response = chain.proceed(request)
            val originalBody = response.peekBody(Long.MAX_VALUE).string()

            try {
                // Parse the response to check the code
                val wrapperCode = JSONObject(originalBody).optInt("code")

                if (wrapperCode != 0) {
                    var oldPath = requestUrl.replace(Regex("https?://.*\\.com/"), "")
                    if (!oldPath.contains("fourk=1")) {
                        oldPath += "&access_key=" + TvUserInfoRepository.accessToken
                        oldPath += "&appkey=" + BiliAppSigner.APP_KEY
                    }

                    val retryUrl = "${BilibiliApi.roamingApi}$oldPath$retryPathSuffix".toHttpUrl()
                    val retryRequest = request.newBuilder()
                        .url(retryUrl)
                        .build()

                    return chain.proceed(retryRequest)
                }
            } catch (e: Exception) {
                throw IOException("Failed to parse response body", e)
            }
        }

        return chain.proceed(request)
    }
}