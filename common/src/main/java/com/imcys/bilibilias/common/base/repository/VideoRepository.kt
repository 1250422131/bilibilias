package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.AID
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.ArchiveCoinsBean
import com.imcys.bilibilias.common.base.model.ArchiveFavouredBean
import com.imcys.bilibilias.common.base.model.ArchiveHasLikeBean
import com.imcys.bilibilias.common.base.model.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.VideoBaseBean
import com.imcys.bilibilias.common.base.model.VideoPlayBean
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(private val httpClient: HttpClient) {
    /**
     * curl -G 'https://api.bilibili.com/x/player/playurl' \
     *     --data-urlencode 'bvid=BV1y7411Q7Eq' \
     *     --data-urlencode 'cid=171776208' \
     *     --data-urlencode 'qn=112' \
     *     --data-urlencode 'fnval=0' \
     *     --data-urlencode 'fnver=0' \
     *     --data-urlencode 'fourk=1' \
     *     -b 'SESSDATA=xxx'
     *
     * [quality] 视频清晰度选择
     * [format] 视频流格式标识 mp4:1 dash:16
     * [allow4KVideo] 是否允许 4K 视频 1080p:0 4k:1
     */
    suspend fun getMp4视频流地址(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ): VideoPlayBean = get视频流地址(
        bvid,
        cid,
        quality,
        format,
        allow4KVideo,
    )

    suspend fun getDash视频流地址(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ): DashVideoPlayBean = get视频流地址(
        bvid,
        cid,
        quality,
        format,
        allow4KVideo,
    )

    private suspend inline fun <reified T> get视频流地址(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ) =
        httpClient.safeGet<T>(BilibiliApi.videoPlayPath) {
            header(REFERER, BILIBILI_URL)
            parameter("bvid", bvid)
            parameter("cid", cid)
            parameter("qn", quality)
            parameter("fnval", format)
            parameter("fourk", allow4KVideo)
        }.getOrThrow()

    /**
     * 视频cid
     */
    suspend fun get实时弹幕(cid: String): ByteArray =
        httpClient.safeGet<ByteArray>(BilibiliApi.videoDanMuPath) {
            parameter("oid", cid)
        }.getOrThrow()

    /**
     * curl -G 'https://api.bilibili.com/x/web-interface/archive/has/like' \
     * --data-urlencode 'aid=39330059' \
     * -b 'SESSDATA=xxx'
     *
     * curl -G 'https://api.bilibili.com/x/web-interface/archive/has/like' \
     * --data-urlencode 'bvid=BV1Bt411z799' \
     * -b 'SESSDATA=xxx'
     */
    suspend fun getVideoDetailsByBvid(bvid: String, videoMate: (VideoBaseBean) -> Unit): Boolean =
        httpClient.safeGet<VideoBaseBean>(BilibiliApi.getVideoDataPath) {
            parameter(BVID, bvid)
        }.onSuccess { videoMate(it) }.isSuccess

    suspend fun getVideoDetailsAvid(avid: String, videoMate: (VideoBaseBean) -> Unit) =
        httpClient.safeGet<VideoBaseBean>(BilibiliApi.getVideoDataPath) {
            parameter(AID, avid)
        }.onSuccess { videoMate(it) }.isSuccess

    suspend fun hasLikeBvid(bvid: String): ArchiveHasLikeBean {
        val like = httpClient.get(BilibiliApi.videoHasLike) {
            parameter(BVID, bvid)
        }.bodyAsText()
        val like2 = httpClient.safeGet<ArchiveHasLikeBean>(BilibiliApi.videoHasLike) {
            parameter(BVID, bvid)
        }.getOrThrow()
        Timber.d(like2.toString())
        return ArchiveHasLikeBean(0)
    }

    suspend fun hasCoinsBvid(bvid: String) =
        httpClient.safeGet<ArchiveCoinsBean>(BilibiliApi.videoHasCoins) {
            parameter(BVID, bvid)
        }.getOrThrow()

    suspend fun hasFavouredBvid(bvid: String) =
        httpClient.safeGet<ArchiveFavouredBean>(BilibiliApi.videoHasFavoured) {
            parameter(AID, bvid)
        }.getOrThrow()
}
