package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.common.utils.ofMap
import com.imcys.common.utils.print
import com.imcys.model.Bangumi
import com.imcys.model.BangumiPlayBean
import com.imcys.model.PlayerInfo
import com.imcys.model.SeasonsSeriesList
import com.imcys.model.VideoCollection
import com.imcys.model.VideoDetails
import com.imcys.model.VideoHasCoins
import com.imcys.model.VideoHasLike
import com.imcys.model.video.Page
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.safeGetText
import com.imcys.network.utils.headerRefBilibili
import com.imcys.network.utils.parameterBV
import com.imcys.network.utils.parameterMID
import com.imcys.network.utils.parameterPageNum
import com.imcys.network.utils.parameterPageSize
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiKeyRepository: WbiKeyRepository,
    private val json: Json,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    /**
     * https://github.com/SocialSisterYi/bilibili-API-collect/pull/437/commits/4403e7ba7ed6853fa00a11371868b917e26b162a
     * ### 查询用户创建的合集
     * https://api.bilibili.com/x/polymer/space/seasons_series_list
     *
     * | 参数名 | 类型 | 内容        | 必要性 | 备注 |
     * | ------ | ---- | ----------- | ------ | ---- |
     * | mid   | num  | 目标用户mid | 必要   |      |
     * | page_num   | num  | 页数 | 必要   |      |
     * | page_size   | num  | 每页项数 | 必要   |   定义域1-20   |
     *
     */
    suspend fun queryCollections(mid: Long, pageNum: Int, pageSize: Int = 20): SeasonsSeriesList =
        withContext(ioDispatcher) {
            httpClient.get("x/polymer/space/seasons_series_list") {
                parameterMID(mid)
                parameterPageNum(pageNum)
                parameterPageSize(pageSize)
            }.body()
        }

    /**
     * 获取合集内容
     * https://api.bilibili.com/x/polymer/space/seasons_archives_list?
     * mid=8047632&
     * season_id=413472&
     * sort_reverse=false&
     * page_num=1&
     * page_size=30
     * mid是up uid,
     * season_id是合集的id
     */
    suspend fun getCollectionContent(
        mid: Long,
        seasonId: Long,
        pageNum: Int,
        pageSize: Int = 30,
        reverse: Boolean = false
    ) = withContext(ioDispatcher) {
        val text = httpClient.get("x/polymer/space/seasons_archives_list") {
            parameterMID(mid)
            parameter("season_id", seasonId)
            parameter("sort_reverse", reverse)
            parameterPageNum(pageNum)
            parameterPageSize(pageSize)
        }.bodyAsText()
        Timber.d("合集内容=$text")
    }

    suspend fun getPlayerPageList(bvid: String): List<Page> = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi2.PLAYER_PAGE_LIST) {
            parameterBV(bvid)
        }.body()
    }

    suspend fun get番剧视频流(epID: String, cid: Long): BangumiPlayBean.Result {
        val text = httpClient.safeGetText(BilibiliApi2.bangumiPlayPath) {
            headerRefBilibili()
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
    suspend fun getEp(id: String): Bangumi = withContext(ioDispatcher) {
        val text = httpClient.get(BilibiliApi2.bangumiVideoDataPath) {
            parameter("ep_id", id)
        }.bodyAsText()
        Timber.tag(TAG).d(text.ofMap()?.print())
        json.decodeFromString(text)
    }

    private val _dashVideo = MutableSharedFlow<Result<PlayerInfo>>(1)
    val dashVideo = _dashVideo.asSharedFlow()
    lateinit var cachePlayerInfo: PlayerInfo
        private set

    /**
     * fnval 默认值已经取到所有值
     */
    suspend fun getDashVideoStream(
        bvid: String,
        cid: Long,
        fnval: Int = 16 or 64 or 128 or 256 or 512 or 1024 or 2048,
        fourk: Int = 1
    ): PlayerInfo = getDashVideoStream(bvid, cid, fnval, fourk, false)

    suspend fun getDashVideoStream(
        bvid: String,
        cid: Long,
        fnval: Int = 16 or 64 or 128 or 256 or 512 or 1024 or 2048,
        fourk: Int = 1,
        useWbi: Boolean = false
    ): PlayerInfo {
        val params: List<Pair<String, Any>> =
            listOf(
                "bvid" to bvid,
                "cid" to cid,
                "qn" to 0,
                "fnval" to fnval,
                "fourk" to fourk,
                "fnver" to 0
            )
        cachePlayerInfo = if (useWbi) {
            withContext(ioDispatcher) {
                val list = wbiKeyRepository.getUserNavToken(params)
                httpClient.get(BilibiliApi2.VIDEO_PLAY_WBI) {
                    toParameter(list)
                }.body()
            }
        } else {
            getVideoStreamAddress(params)
        }
        return cachePlayerInfo
    }

    fun HttpRequestBuilder.toParameter(parameters: List<Pair<String, Any>>) {
        parameters.forEach {
            url.parameters.append(it.first, it.second.toString())
        }
    }

    /**
     * @param fnval 视频流格式标识 mp4值恒为1
     * @param fourk 是否允许 4K 视频 1080p为0 4k为1
     * @param fnver 恒为0
     */
    private suspend inline fun <reified T> getVideoStreamAddress(
        params: List<Pair<String, Any>>
    ): T = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi2.videoPlayPath) {
            headerRefBilibili()
            toParameter(params)
        }.body()
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
    lateinit var cacheVideoView: VideoDetails
        private set

    /**
     * aid=39330059
     *
     * bvid=BV1Bt411z799
     */
    suspend fun getVideoDetailsByBvid(bvid: String): VideoDetails = withContext(ioDispatcher) {
        cacheVideoView = httpClient.get(BilibiliApi2.getVideoDataPath) {
            parameterBV(bvid)
        }.body()
        cacheVideoView
    }

    suspend fun getVideoDetailsByAid(avid: String): VideoDetails = withContext(ioDispatcher) {
        cacheVideoView = httpClient.get(BilibiliApi2.getVideoDataPath) {
            parameter("aid", avid)
        }.body()
        cacheVideoView
    }

    /**
     * 点赞
     */
    suspend fun hasLike(bvid: String): Boolean = withContext(ioDispatcher) {
        val hasLike = httpClient.get(BilibiliApi2.videoHasLike) {
            parameterBV(bvid)
        }.body<VideoHasLike>()
        hasLike.like
    }

    /**
     * 投币
     */
    suspend fun hasCoins(bvid: String): Boolean = withContext(ioDispatcher) {
        val hasCoins = httpClient.get(BilibiliApi2.videoHasCoins) {
            parameterBV(bvid)
        }.body<VideoHasCoins>()
        hasCoins.coins
    }

    /**
     * 收藏
     *
     * aid=46281123
     *
     * aid=BV1Bb411H7Dv
     */
    suspend fun hasCollection(bvid: String): Boolean = withContext(ioDispatcher) {
        val collection = httpClient.get(BilibiliApi2.videoHasCollection) {
            parameter("aid", bvid)
        }.body<VideoCollection>()
        collection.isFavoured
    }

    suspend fun shortLink(url: String): String = withContext(ioDispatcher) {
        httpClient.get(url)
            .body<HttpResponse>()
            .request
            .url
            .toString()
    }
}

private const val TAG = "VideoRepository"
