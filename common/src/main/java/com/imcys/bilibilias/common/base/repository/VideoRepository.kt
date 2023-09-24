package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.AID
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.BangumiPlayBean
import com.imcys.bilibilias.common.base.model.BangumiSeasonBean
import com.imcys.bilibilias.common.base.model.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.VideoDetails
import com.imcys.bilibilias.common.base.model.VideoPlayBean
import com.imcys.bilibilias.common.base.model.video.VideoHasCoins
import com.imcys.bilibilias.common.base.model.video.VideoHasLike
import com.imcys.bilibilias.common.base.model.video.VideoCollection
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(private val httpClient: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true }
    suspend fun get番剧视频流(epID: String, cid: Long): BangumiPlayBean.Result {
        val text = httpClient.get(BilibiliApi.bangumiPlayPath) {
            header(REFERER, BILIBILI_URL)
            parameter("ep_id", epID)
            parameter("cid", cid)
            parameter("qn", 64)
            parameter("fnval", 0)
            parameter("fourk", 1)
        }.bodyAsText()
        Timber.tag(TAG).d(text)
        return json.decodeFromString<BangumiPlayBean>(text).result
    }

    suspend fun get剧集基本信息(epID: String): BangumiSeasonBean.ResultBean {
        val text = httpClient.get(BilibiliApi.bangumiVideoDataPath) {
            parameter("ep_id", epID)
        }.bodyAsText()
        return json.decodeFromString<BangumiSeasonBean>(text).result
    }

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
     * @param quality 视频清晰度选择
     * @param format 视频流格式标识 mp4:1 dash:16
     * @param allow4KVideo 是否允许 4K 视频 1080p:0 4k:1
     */
    suspend fun getMp4视频流地址(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ): VideoPlayBean = get视频流地址(bvid, cid, quality, format, allow4KVideo)

    suspend fun getDash视频流地址(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ): DashVideoPlayBean = get视频流地址(bvid, cid, quality, format, allow4KVideo)

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

    private val _testSharedFlow = MutableSharedFlow<VideoDetails>(1, 2)
    val testSharedFlow = _testSharedFlow.asSharedFlow()

    /**
     * aid=39330059
     *
     * bvid=BV1Bt411z799
     */
    suspend fun getVideoDetailsByBvid(bvid: String) {
        getVideoDetails<VideoDetails>(BVID to bvid)
            .onSuccess {
                _testSharedFlow.emit(it)
            }
    }
    suspend fun getVideoDetailsAvid(avid: String) {
        getVideoDetails<VideoDetails>(AID to avid)
            .onSuccess {
                _testSharedFlow.emit(it)
            }
    }
    private suspend inline fun <reified T> getVideoDetails(pair: Pair<String, String>) =
        httpClient.safeGet<T>(BilibiliApi.getVideoDataPath) {
            parameter(pair.first, pair.second)
        }

    /**
     * 点赞
     */
    suspend fun hasLike(bvid: String): VideoHasLike {
        val like2 = httpClient.safeGet<VideoHasLike>(BilibiliApi.videoHasLike) {
            parameter(BVID, bvid)
        }.getOrThrow()
        Timber.d(like2.toString())
        return VideoHasLike(0)
    }

    /**
     * 投币
     */
    suspend fun hasCoins(bvid: String) =
        httpClient.safeGet<VideoHasCoins>(BilibiliApi.videoHasCoins) {
            parameter(BVID, bvid)
        }.getOrThrow()

    /**
     * 收藏
     *
     * aid=46281123
     *
     * aid=BV1Bb411H7Dv
     */
    suspend fun hasCollection(id: String) =
        httpClient.safeGet<VideoCollection>(BilibiliApi.videoHasCollection) {
            parameter(AID, id)
        }.getOrThrow()
}

private const val TAG = "VideoRepository"
