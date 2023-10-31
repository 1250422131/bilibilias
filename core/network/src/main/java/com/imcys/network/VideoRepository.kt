package com.imcys.network

import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.common.utils.ofMap
import com.imcys.common.utils.print
import com.imcys.model.BangumiPlayBean
import com.imcys.model.DashVideoPlayBean
import com.imcys.model.VideoCollection
import com.imcys.model.VideoDetails
import com.imcys.model.VideoHasCoins
import com.imcys.model.VideoHasLike
import com.imcys.model.VideoPageListData
import com.imcys.network.api.BilibiliApi2
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.request
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.readBytes
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
        val result = httpClient.safeGet<List<VideoPageListData>>(BilibiliApi2.videoPageListPath) {
            parameter("bvid", bvid)
        }
        return result
    }

    suspend fun get番剧视频流(epID: String, cid: Long): BangumiPlayBean.Result {
        val text = httpClient.safeGetText(BilibiliApi2.bangumiPlayPath) {
            header(HttpHeaders.Referrer, "BILIBILI_URL")
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
        val text = httpClient.safeGetText(BilibiliApi2.bangumiVideoDataPath) {
            parameter("ep_id", id)
        }
        Timber.tag(TAG).d(text.ofMap()?.print())
        BangumiPlayBean
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
            httpClient.safeGet(BilibiliApi2.VIDEO_PLAY_WBI) {
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
    ): Result<T> = httpClient.safeGet(BilibiliApi2.videoPlayPath) {
        header(HttpHeaders.Referrer, "https://www.bilibili.com")
        toParameter(params)
    }

    /**
     * oid = 视频cid
     */
    suspend fun getDanmakuXml(cid: Long): Flow<Result<ByteArray>> = flow {
        val bytes = httpClient.get(BilibiliApi2.videoDanMuPath) {
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
    // fun getRealTimeDanmaku(cid: Long, segmentIndex: Int, type: Int = 1): Flow<Result<Dm.DmSegMobileReply>> {
    //     // av810872(cid=1176840)
    //     val result = flow {
    //         val text = httpClient.get(BilibiliApi2.thisVideoDanmakuPath) {
    //             parameter("oid", cid)
    //             parameter("segment_index", segmentIndex)
    //             parameter("type", type)
    //         }.bodyAsChannel()
    //
    //         val parseFrom = Dm.DmSegMobileReply.parseFrom(text.toInputStream())
    //         emit(parseFrom)
    //     }.asResult()
    //     return result
    // }

    // suspend fun getRealTimeDanmaku(
    //     cid: Long,
    //     aid: Long,
    //     segmentIndex: Int = 1,
    //     type: Int = 1,
    //     useWbi: Boolean = true
    // ): Flow<Result<Dm.DmSegMobileReply>> {
    //     return if (useWbi) {
    //         flow {
    //             val navToken = wbiKeyRepository.getUserNavToken(
    //                 listOf(
    //                     "oid" to cid,
    //                     "segment_index" to segmentIndex,
    //                     "type" to type,
    //                     "pid" to aid,
    //                 )
    //             )
    //             val channel = httpClient.get(BilibiliApi2.thisVideoDanmakuWbiPath) {
    //                 navToken.forEach {
    //                     parameter(it.first, it.second)
    //                 }
    //             }.bodyAsChannel()
    //             val parseFrom = Dm.DmSegMobileReply.parseFrom(channel.toInputStream())
    //             emit(parseFrom)
    //         }.asResult()
    //     } else {
    //         getRealTimeDanmaku(cid, segmentIndex, type)
    //     }
    // }

    private val _videoDetails2 = MutableSharedFlow<Result<VideoDetails>>(1)
    val videoDetails2 = _videoDetails2.asSharedFlow()

    /**
     * aid=39330059
     *
     * bvid=BV1Bt411z799
     */
    suspend fun getVideoDetailsByBvid(bvid: String): Result<VideoDetails> {
        val videoDetailsResult = httpClient.safeGet<VideoDetails>(BilibiliApi2.getVideoDataPath) {
            parameterBV(bvid)
        }
        _videoDetails2.emit(videoDetailsResult)
        return videoDetailsResult
    }

    suspend fun getVideoDetailsByAid(avid: String): Result<VideoDetails> {
        val videoDetailsResult = httpClient.safeGet<VideoDetails>(BilibiliApi2.getVideoDataPath) {
            parameter("aid", avid)
        }
        _videoDetails2.emit(videoDetailsResult)
        return videoDetailsResult
    }

    /**
     * 点赞
     */
    suspend fun hasLike(bvid: String): Boolean {
        val hasLike = httpClient.get(BilibiliApi2.videoHasLike) {
            parameterBV(bvid)
        }.body<VideoHasLike>()
        return hasLike.like
    }

    /**
     * 投币
     */
    suspend fun hasCoins(bvid: String): Boolean {
        val hasCoins = httpClient.get(BilibiliApi2.videoHasCoins) {
            parameterBV(bvid)
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
        val collection = httpClient.get(BilibiliApi2.videoHasCollection) {
            parameter("aid", bvid)
        }.body<VideoCollection>()
        return collection.isFavoured
    }

    suspend fun shortLink(url: String): String? {
        return when (val httpResponse: Result<HttpResponse> = httpClient.safeGet(url)) {
            is Result.Error -> null
            Result.Loading -> null
            is Result.Success -> httpResponse.data.request.url.toString()
        }
    }
}

fun HttpRequestBuilder.parameterBV(bvid: String): Unit = parameter("bvid", bvid)

private const val TAG = "VideoRepository"