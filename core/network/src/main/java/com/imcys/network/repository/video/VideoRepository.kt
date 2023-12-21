package com.imcys.network.repository.video

import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.VideoUtils
import com.imcys.model.Bangumi
import com.imcys.model.BangumiPlayBean
import com.imcys.model.PlayerInfo
import com.imcys.model.SeasonsSeriesList
import com.imcys.model.VideoDetails
import com.imcys.model.VideoHasCoins
import com.imcys.model.VideoHasLike
import com.imcys.model.video.PageData
import com.imcys.model.video.ViewDetailAndPlayUrl
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.safeGetText
import com.imcys.network.utils.headerRefBilibili
import com.imcys.network.utils.parameterBV
import com.imcys.network.utils.parameterMID
import com.imcys.network.utils.parameterPageNum
import com.imcys.network.utils.parameterPageSize
import com.imcys.network.wbiGet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val client: HttpClient,
    private val json: Json,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : IVideoDataSources {
    private val cacheViewDetail = ArraySet<VideoDetails>()
    private val cachePlayerPlayUrl = ArrayMap<String, PlayerInfo>()

    /**
     * https://github.com/SocialSisterYi/bilibili-API-collect/pull/437/commits/4403e7ba7ed6853fa00a11371868b917e26b162a
     *
     * ### 查询用户创建的合集
     * https://api.bilibili.com/x/polymer/space/seasons_series_list | 参数名 | 类型
     * | 内容 | 必要性 | 备注 | |-----------|-----|---------|-----|---------| | mid |
     * num | 目标用户mid | 必要 | | | page_num | num | 页数 | 必要 | | | page_size | num
     * | 每页项数 | 必要 | 定义域1-20 |
     */
    suspend fun queryCollections(mid: Long, pageNum: Int, pageSize: Int = 20): SeasonsSeriesList =
        withContext(ioDispatcher) {
            client.get("x/polymer/space/seasons_series_list") {
                parameterMID(mid)
                parameterPageNum(pageNum)
                parameterPageSize(pageSize)
            }.body()
        }

    /**
     * 获取合集内容 https://api.bilibili.com/x/polymer/space/seasons_archives_list?
     * mid=8047632& season_id=413472& sort_reverse=false&
     * page_num=1& page_size=30 mid是up uid, season_id是合集的id
     */
    suspend fun getCollectionContent(
        mid: Long,
        seasonId: Long,
        pageNum: Int,
        pageSize: Int = 30,
        reverse: Boolean = false
    ) = withContext(ioDispatcher) {
        val text = client.get("x/polymer/space/seasons_archives_list") {
            parameterMID(mid)
            parameter("season_id", seasonId)
            parameter("sort_reverse", reverse)
            parameterPageNum(pageNum)
            parameterPageSize(pageSize)
        }.bodyAsText()
        Timber.d("合集内容=$text")
    }

    suspend fun getPlayerPageList(bvid: String): List<PageData> = withContext(ioDispatcher) {
        client.get(BilibiliApi2.PLAYER_PAGE_LIST) {
            parameterBV(bvid)
        }.body()
    }

    suspend fun get番剧视频流(epID: String, cid: Long): BangumiPlayBean.Result {
        val text = client.safeGetText(BilibiliApi2.bangumiPlayPath) {
            headerRefBilibili()
            parameter("ep_id", epID)
            parameter("cid", cid)
            parameter("qn", 64)
            parameter("fnval", 0)
            parameter("fourk", 1)
        }
        return json.decodeFromString<BangumiPlayBean>(text.toString()).result
    }

    suspend fun getEp(id: String): Bangumi = withContext(ioDispatcher) {
        val text = client.get(BilibiliApi2.bangumiVideoDataPath) {
            parameter("ep_id", id)
        }.bodyAsText()
        json.decodeFromString(text)
    }

    /** 点赞 */
    suspend fun hasLike(bvid: String): Boolean = withContext(ioDispatcher) {
        val hasLike = client.get(BilibiliApi2.videoHasLike) {
            parameterBV(bvid)
        }.body<VideoHasLike>()
        hasLike.like
    }

    /** 投币 */
    suspend fun hasCoins(bvid: String): Boolean = withContext(ioDispatcher) {
        val hasCoins = client.get(BilibiliApi2.videoHasCoins) {
            parameterBV(bvid)
        }.body<VideoHasCoins>()
        hasCoins.coins
    }

    suspend fun shortLink(url: String): String = withContext(ioDispatcher) {
        client.get(url)
            .body<HttpResponse>()
            .request
            .url
            .toString()
    }

    @Deprecated("")
    override suspend fun getViewDetailAndPlayUrl(bvid: String): ViewDetailAndPlayUrl {
        val detail = getDetail(bvid)
        val playerPlayUrl = getPlayerPlayUrl(bvid, detail.cid)
        return ViewDetailAndPlayUrl(
            detail.aid,
            detail.bvid,
            detail.cid,
            detail.title,
            playerPlayUrl.dash,
            detail,
            playerPlayUrl
        )
    }

    override suspend fun getDetail(bvid: String): VideoDetails = withContext(ioDispatcher) {
        val detail = cacheViewDetail.find { it.bvid == bvid }
        if (detail == null) {
            val body = client.get(BilibiliApi2.VIEW_DETAIL) {
                parameterBV(bvid)
            }.body<VideoDetails>()
            cacheViewDetail.add(body)
            body
        } else {
            detail
        }
    }

    override suspend fun getDetail(aid: Long): VideoDetails = withContext(ioDispatcher) {
        val detail = cacheViewDetail.find { it.aid == aid }
        if (detail == null) {
            val bv = VideoUtils.av2bv(aid)
            val detail1 = getDetail(bv)
            cacheViewDetail.add(detail1)
            detail1
        } else {
            detail
        }
    }

    override suspend fun getPlayerPlayUrl(bvid: String, cid: Long): PlayerInfo {
        return cachePlayerPlayUrl.getOrElse("$bvid-$cid") {
            withContext(ioDispatcher) {
                client.wbiGet(BilibiliApi2.PLAYER_PLAY_URL_WBI) {
                    parameter("bvid", bvid)
                    parameter("cid", cid)
                    parameter("fnval", IVideoDataSources.REQUIRED_ALL.toString())
                    parameter("fourk", "1")
                    parameter("platform", "pc")
                }.body<PlayerInfo>()
            }
        }
    }
}