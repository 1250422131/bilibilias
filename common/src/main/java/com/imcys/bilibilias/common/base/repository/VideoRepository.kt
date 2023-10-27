package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.constant.AID
import com.imcys.bilibilias.common.base.constant.BILIBILI_URL
import com.imcys.bilibilias.common.base.constant.BVID
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.asResult
import com.imcys.bilibilias.common.base.extend.ofMap
import com.imcys.bilibilias.common.base.extend.print
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.extend.safeGetText
import com.imcys.bilibilias.common.base.model.BangumiPlayBean
import com.imcys.bilibilias.common.base.model.video.DashVideoPlayBean
import com.imcys.bilibilias.common.base.model.video.VideoCollection
import com.imcys.bilibilias.common.base.model.video.VideoDetails
import com.imcys.bilibilias.common.base.model.video.VideoHasCoins
import com.imcys.bilibilias.common.base.model.video.VideoHasLike
import com.imcys.bilibilias.common.base.model.video.VideoPageListData
import com.imcys.bilibilias.dm.Dm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiKeyRepository: WbiKeyRepository,
    private val json: Json
) {

    suspend fun getVideoPageList(bvid: String): Result<List<VideoPageListData>> {
        val result = httpClient.safeGet<List<VideoPageListData>>(BilibiliApi.videoPageListPath) {
            parameter(BVID, bvid)
        }
        return result
    }

    suspend fun get番剧视频流(epID: String, cid: Long): BangumiPlayBean.Result {
        val text = httpClient.safeGetText(BilibiliApi.bangumiPlayPath) {
            header(HttpHeaders.Referrer, BILIBILI_URL)
            parameter("ep_id", epID)
            parameter("cid", cid)
            parameter("qn", 64)
            parameter("fnval", 0)
            parameter("fourk", 1)
        }
        Timber.tag(TAG).d(text.toString())
        return json.decodeFromString<BangumiPlayBean>(text.toString()).result
    }

    private val _bangumi = MutableSharedFlow<Result<String>>(1)
    val bangumi = _bangumi.asSharedFlow()
    suspend fun getEp(id: String) {
        val text = httpClient.safeGetText(BilibiliApi.bangumiVideoDataPath) {
            parameter("ep_id", id)
        }
        Timber.tag(TAG).d(text.ofMap()?.print())
        _bangumi.emit(text)
    }

    private val _dashVideo = MutableSharedFlow<Result<DashVideoPlayBean>>(1)
    val dashVideo = _dashVideo.asSharedFlow()

    /**
     * fnval 默认值已经取到所有值
     */
    suspend fun getDashVideoStream(
        bvid: String,
        cid: Long,
        fnval: Int = 16 or 64 or 128 or 256 or 512 or 1024 or 2048,
        fourk: Int = 1
    ): Result<DashVideoPlayBean> {
        val value = getDashVideoStream(bvid, cid, fnval, fourk, false)
        _dashVideo.emit(value)
        Timber.d("bv=$bvid,cid=$cid,fnval=$fnval,fourk=$fourk")
        return value
    }

    suspend fun getDashVideoStream(
        bvid: String,
        cid: Long,
        fnval: Int = 16 or 64 or 128 or 256 or 512 or 1024 or 2048,
        fourk: Int = 1,
        useWbi: Boolean = false
    ): Result<DashVideoPlayBean> {
        val params: List<Pair<String, Any>> =
            listOf(
                "bvid" to bvid,
                "cid" to cid,
                "qn" to 0,
                "fnval" to fnval,
                "fourk" to fourk,
                "fnver" to 0
            )
        return if (useWbi) {
            val list = wbiKeyRepository.getUserNavToken(params)
            httpClient.safeGet(BilibiliApi.VIDEO_PLAY_WBI) {
                toParameter(list)
            }
        } else {
            getVideoStreamAddress(params)
        }
    }

    fun HttpRequestBuilder.toParameter(parameters: List<Pair<String, Any>>) {
        parameters.forEach {
            parameter(it.first, it.second)
        }
    }

    /**
     * @param fnval 视频流格式标识 mp4值恒为1
     * @param fourk 是否允许 4K 视频 1080p为0 4k为1
     * @param fnver 恒为0
     */
    private suspend inline fun <reified T> getVideoStreamAddress(
        params: List<Pair<String, Any>>
    ): Result<T> = httpClient.safeGet(BilibiliApi.videoPlayPath) {
        header(HttpHeaders.Referrer, BILIBILI_URL)
        toParameter(params)
    }

    /**
     * oid = 视频cid
     */
    suspend fun getDanmakuXml(cid: Long): Flow<Result<ByteArray>> = flow {
        val bytes = httpClient.get(BilibiliApi.videoDanMuPath) {
            parameter("oid", cid)
        }
            .bodyAsChannel()
            .readRemaining()
            .readBytes()
        emit(bytes)
    }.asResult()

    /**
     * https://api.bilibili.com/x/v2/dm/wbi/web/seg.so?
     * type=1&
     * oid=1283745701&
     * pid=619092919&
     * segment_index=1&
     * pull_mode=1&
     * ps=0&
     * pe=120000&
     * web_location=1315873&
     * w_rid=03838b4362dc45b1ab4784e6cd1a6f18&
     * wts=1697211774
     */
    fun getRealTimeDanmaku(cid: Long, segmentIndex: Int, type: Int = 1): Flow<Result<Dm.DmSegMobileReply>> {
        // av810872(cid=1176840)
        val result = flow {
            val text = httpClient.get(BilibiliApi.thisVideoDanmakuPath) {
                parameter("oid", cid)
                parameter("segment_index", segmentIndex)
                parameter("type", type)
            }.bodyAsChannel()

            val parseFrom = Dm.DmSegMobileReply.parseFrom(text.toInputStream())
            emit(parseFrom)
        }.asResult()
        return result
    }

    suspend fun getRealTimeDanmaku(
        cid: Long,
        aid: Long,
        segmentIndex: Int = 1,
        type: Int = 1,
        useWbi: Boolean = true
    ): Flow<Result<Dm.DmSegMobileReply>> {
        return if (useWbi) {
            flow {
                val navToken = wbiKeyRepository.getUserNavToken(
                    listOf(
                        "oid" to cid,
                        "segment_index" to segmentIndex,
                        "type" to type,
                        "pid" to aid,
                    )
                )
                val channel = httpClient.get(BilibiliApi.thisVideoDanmakuWbiPath) {
                    navToken.forEach {
                        parameter(it.first, it.second)
                    }
                }.bodyAsChannel()
                val parseFrom = Dm.DmSegMobileReply.parseFrom(channel.toInputStream())
                emit(parseFrom)
            }.asResult()
        } else {
            getRealTimeDanmaku(cid, segmentIndex, type)
        }
    }

    private val _videoDetails2 = MutableSharedFlow<Result<VideoDetails>>(1)
    val videoDetails2 = _videoDetails2.asSharedFlow()

    /**
     * aid=39330059
     *
     * bvid=BV1Bt411z799
     */
    suspend fun getVideoDetailsByBvid(bvid: String) {
        val result = httpClient.safeGet<VideoDetails>(BilibiliApi.getVideoDataPath) {
            parameter(BVID, bvid)
        }
        _videoDetails2.emit(result)
    }

    suspend fun getVideoDetailsByAid(avid: String) {
        val result = httpClient.safeGet<VideoDetails>(BilibiliApi.getVideoDataPath) {
            parameter(AID, avid)
        }
        _videoDetails2.emit(result)
    }

    /**
     * 点赞
     */
    suspend fun hasLike(bvid: String): Boolean {
        val hasLike = httpClient.get(BilibiliApi.videoHasLike) {
            parameter(BVID, bvid)
        }.body<VideoHasLike>()
        return hasLike.like
    }

    /**
     * 投币
     */
    suspend fun hasCoins(bvid: String): Boolean {
        val hasCoins = httpClient.get(BilibiliApi.videoHasCoins) {
            parameter(BVID, bvid)
        }.body<VideoHasCoins>()
        return hasCoins.coins
    }

    /**
     * 收藏
     *
     * aid=46281123
     *
     * aid=BV1Bb411H7Dv
     */
    suspend fun hasCollection(bvid: String): Boolean {
        val collection = httpClient.get(BilibiliApi.videoHasCollection) {
            parameter(AID, bvid)
        }.body<VideoCollection>()
        return collection.isFavoured
    }

    suspend fun shortLink(url: String) = httpClient.safeGet<HttpResponse>(url)
}

private const val TAG = "VideoRepository"
