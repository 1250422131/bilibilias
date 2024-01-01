package com.imcys.network.repository.video

import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.VideoUtils
import com.imcys.model.Bangumi
import com.imcys.model.BangumiPlayBean
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails
import com.imcys.model.video.ArchiveCoins
import com.imcys.model.video.ArchiveHasLike
import com.imcys.model.video.VideoFavoured
import com.imcys.model.video.ViewDetailAndPlayUrl
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.safeGetText
import com.imcys.network.utils.headerRefBilibili
import com.imcys.network.utils.parameterBV
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

    /**
     * 点赞
     */
    override suspend fun hasLike(bvid: String): ArchiveHasLike = withContext(ioDispatcher) {
        val archiveText = client.get(BilibiliApi2.ARCHIVE_HAS_LIKE) {
            parameterBV(bvid)
        }.bodyAsText()
        val hasLike = json.decodeFromString<ArchiveHasLike>(archiveText)
        hasLike
    }

    override suspend fun hasCoin(bvid: String): ArchiveCoins = withContext(ioDispatcher) {
        client.get(BilibiliApi2.ARCHIVE_COINS) {
            parameterBV(bvid)
        }.body<ArchiveCoins>()
    }

    override suspend fun hasFavoured(bvid: String): VideoFavoured = withContext(ioDispatcher) {
        client.get(BilibiliApi2.VIDEO_FAVOURED) {
            parameter("aid", bvid)
        }.body<VideoFavoured>()
    }

    suspend fun shortLink(url: String): String = withContext(ioDispatcher) {
        client.get(url)
            .body<HttpResponse>()
            .request
            .url
            .toString()
    }

    @Deprecated("domain 有相同 api")
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
        client.get(BilibiliApi2.VIEW_DETAIL) {
            parameterBV(bvid)
        }.body<VideoDetails>()
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
