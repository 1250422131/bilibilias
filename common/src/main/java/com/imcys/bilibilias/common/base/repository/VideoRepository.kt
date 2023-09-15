package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.AID
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.VideoBaseBean
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
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
    suspend fun getVideoDetailsBvid(bvid: String, videoMate: (VideoBaseBean) -> Unit): Boolean =
        httpClient.safeGet<VideoBaseBean>(BilibiliApi.getVideoDataPath) {
            parameter(BVID, bvid)
        }.onSuccess { videoMate(it) }.isSuccess

    suspend fun getVideoDetailsAvid(avid: String, videoMate: (VideoBaseBean) -> Unit) =
        httpClient.safeGet<VideoBaseBean>(BilibiliApi.getVideoDataPath) {
            parameter(AID, avid)
        }.onSuccess { videoMate(it) }.isSuccess

    suspend fun hasLikeAid(aid: String) = httpClient.get(BilibiliApi.videoHasLike) {
        parameter(AID, aid)
    }

    suspend fun hasLikeBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasLike) {
        parameter(BVID, bvid)
    }

    suspend fun hasCoinsAid(aid: String) = httpClient.get(BilibiliApi.videoHasCoins) {
        parameter(AID, aid)
    }

    suspend fun hasCoinsBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasCoins) {
        parameter(BVID, bvid)
    }

    suspend fun hasFavouredAid(aid: String) = httpClient.get(BilibiliApi.videoHasFavoured) {
        parameter(AID, aid)
    }

    suspend fun hasFavouredBvid(bvid: String) = httpClient.get(BilibiliApi.videoHasFavoured) {
        parameter(BVID, bvid)
    }
}
