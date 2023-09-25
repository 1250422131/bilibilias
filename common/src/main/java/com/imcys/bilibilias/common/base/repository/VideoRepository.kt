package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.AID
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.constant.REFERER
import com.imcys.bilibilias.common.base.extend.ofMap
import com.imcys.bilibilias.common.base.extend.print
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.BangumiPlayBean
import com.imcys.bilibilias.common.base.model.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.bangumi.Bangumi
import com.imcys.bilibilias.common.base.model.video.VideoCollection
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoHasCoins
import com.imcys.bilibilias.common.base.model.video.VideoHasLike
import com.imcys.bilibilias.common.base.model.video.VideoPlayDetails
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(private val httpClient: HttpClient) {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
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

    private val _bangumi = MutableSharedFlow<Bangumi.Result>(1, 1)
    val bangumi = _bangumi.asSharedFlow()
    suspend fun get剧集基本信息(id: String) {
        val text = httpClient.get(BilibiliApi.bangumiVideoDataPath) {
            parameter("ep_id", id)
        }.bodyAsText()
        val result = json.decodeFromString<Bangumi>(text).result
        Timber.tag(TAG).d(result.ofMap()?.print())
        _bangumi.emit(result)
    }

    private val _videoPlayDetailsFlow = MutableSharedFlow<VideoPlayDetails>(1, 1)
    val videoPlayDetailsFlow = _videoPlayDetailsFlow.asSharedFlow()

    /**
     * ```
     * bvid=BV1y7411Q7Eq
     * cid=171776208
     * qn=112
     * fnval=0
     * fnver=0
     * fourk=1
     * ```
     * @param quality 视频清晰度选择
     * @param format 视频流格式标识 mp4:1 dash:16
     * @param allow4KVideo 是否允许 4K 视频 1080p:0 4k:1
     * getMP4VideoStream
     */
    suspend fun getMP4VideoStream(
        bvid: String,
        cid: Long,
        quality: Int = 64,
        format: Int = 1,
        allow4KVideo: Int = 1
    ): VideoPlayDetails = getVideoStreamAddress(bvid, cid, quality, format, allow4KVideo)

    suspend fun getDashVideoStream(
        bvid: String,
        cid: Long,
        quality: Int,
        format: Int,
        allow4KVideo: Int
    ): DashVideoPlayBean = getVideoStreamAddress(bvid, cid, quality, format, allow4KVideo)

    private suspend inline fun <reified T> getVideoStreamAddress(
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

    private val _videoDetailsFlow = MutableSharedFlow<VideoDetails>(1, 1)
    val videoDetailsFlow = _videoDetailsFlow.asSharedFlow()

    /**
     * aid=39330059
     *
     * bvid=BV1Bt411z799
     */
    suspend fun getVideoDetailsByBvid(bvid: String) {
        getVideoDetails<VideoDetails>(BVID to bvid)
            .onSuccess {
                _videoDetailsFlow.emit(it)
            }
    }

    suspend fun getVideoDetailsAvid(avid: String) {
        getVideoDetails<VideoDetails>(AID to avid)
            .onSuccess {
                _videoDetailsFlow.emit(it)
            }
    }

    private suspend inline fun <reified T> getVideoDetails(pair: Pair<String, String>) =
        httpClient.safeGet<T>(BilibiliApi.getVideoDataPath) {
            parameter(pair.first, pair.second)
        }

    private val _isLikeFlow = MutableSharedFlow<Boolean>(1, 1)
    val isLikeFlow = _isLikeFlow.asSharedFlow()

    /**
     * 点赞
     */
    suspend fun hasLike(bvid: String) {
        httpClient.safeGet<VideoHasLike>(BilibiliApi.videoHasLike) {
            parameter(BVID, bvid)
        }.onSuccess {
            _isLikeFlow.emit(it.like)
        }
    }

    private val _isCoinsFlow = MutableStateFlow(false)
    val isCoinsFlow = _isCoinsFlow.asStateFlow()

    /**
     * 投币
     */
    suspend fun hasCoins(bvid: String) {
        httpClient.safeGet<VideoHasCoins>(BilibiliApi.videoHasCoins) {
            parameter(BVID, bvid)
        }.onSuccess {
            _isCoinsFlow.emit(it.coins)
        }
    }

    private val _isCollectionFlow = MutableStateFlow(false)
    val isCollectionFlow = _isCollectionFlow.asStateFlow()

    /**
     * 收藏
     *
     * aid=46281123
     *
     * aid=BV1Bb411H7Dv
     */
    suspend fun hasCollection(id: String) {
        httpClient.safeGet<VideoCollection>(BilibiliApi.videoHasCollection) {
            parameter(AID, id)
        }.onSuccess {
            _isCollectionFlow.emit(it.isFavoured)
        }
    }
}

private const val TAG = "VideoRepository"
