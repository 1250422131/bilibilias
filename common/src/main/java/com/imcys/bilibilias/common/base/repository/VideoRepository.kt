package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class VideoRepository @Inject constructor(private val httpClient: HttpClient) {

    /**
     * curl -G 'https://api.bilibili.com/x/web-interface/archive/has/like' \
     * --data-urlencode 'aid=39330059' \
     * -b 'SESSDATA=xxx'
     *
     * curl -G 'https://api.bilibili.com/x/web-interface/archive/has/like' \
     * --data-urlencode 'bvid=BV1Bt411z799' \
     * -b 'SESSDATA=xxx'
     */

    suspend fun hasLikeAid(aid: String) = httpClient.get(BilibiliApi.videoHasLike) {
        parameter("aid", aid)
    }

    suspend fun hasLikeBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasLike) {
        parameter("bvid", bvid)
    }

    suspend fun hasCoinsAid(aid: String) = httpClient.get(BilibiliApi.videoHasCoins) {
        parameter("aid", aid)
    }

    suspend fun hasCoinsBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasCoins) {
        parameter("bvid", bvid)
    }

    suspend fun hasFavouredAid(aid: String) = httpClient.get(BilibiliApi.videoHasFavoured) {
        parameter("aid", aid)
    }

    suspend fun hasFavouredBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasFavoured) {
        parameter("bvid", bvid)
    }
}
