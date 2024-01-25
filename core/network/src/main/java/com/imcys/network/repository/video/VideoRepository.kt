package com.imcys.network.repository.video

import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import com.imcys.common.utils.ConvertUtil
import com.imcys.model.NetworkPlayerPlayUrl
import com.imcys.model.PgcPlayUrl
import com.imcys.model.PgcViewSeason
import com.imcys.model.ViewDetail
import com.imcys.model.video.ArchiveCoins
import com.imcys.model.video.ArchiveHasLike
import com.imcys.model.video.VideoFavoured
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.headerRefBilibili
import com.imcys.network.utils.parameterBV
import com.imcys.network.utils.parameterCID
import com.imcys.network.utils.parameterEpId
import com.imcys.network.wbiGet
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.request
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val client: HttpClient,
    private val json: Json
) : IVideoDataSources {
    private val cacheView = ArraySet<ViewDetail>()
    private val cachePlayerPlayUrl = ArrayMap<String, NetworkPlayerPlayUrl>()

    /**
     * /pgc/player/web/v2/playurl? support_multi_audio=true&
     * avid=495494010& cid=1385942568& qn=116& fnver=0& fnval=4048&
     * fourk=1& gaia_source=& from_client=BROWSER& ep_id=809818&
     * session=5bafa0564461e25b2d751bd2eb1d8816& drm_tech_type=2
     */
    override suspend fun 获取剧集播放地址(epID: Long, aId: Long, cId: Long): PgcPlayUrl {
        Napier.d { "获取番剧视频流 ep=$epID, aId=$aId, cId=$cId" }
        return client.get(BilibiliApi2.PGC_PLAY_URL) {
            headerRefBilibili()
            parameter("avid", aId)
            parameterCID(cId)
            parameterEpId(epID)
            parameter("support_multi_audio", "true")
            parameter("qn", "116")
            parameter("fnver", "0")
            parameter("fnval", "4048")
            parameter("fourk", "1")
            parameter("from_client", "BROWSER")
            parameter("drm_tech_type", "2")
        }.body()
    }

    override suspend fun 获取剧集基本信息(epId: String): PgcViewSeason {
        Napier.d { "获取剧集详情 epId=$epId" }
        return client.get(BilibiliApi2.PGC_VIEW_SEASON) {
            parameterEpId(epId)
        }.body()
    }

    /**
     * 点赞
     */
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun hasLike(bvid: String): ArchiveHasLike {
        val text = client.get(BilibiliApi2.ARCHIVE_HAS_LIKE) {
            parameterBV(bvid)
        }.bodyAsChannel()
        return json.decodeFromStream(text.toInputStream())
    }

    override suspend fun hasCoin(bvid: String): ArchiveCoins =
        client.get(BilibiliApi2.ARCHIVE_COINS) {
            parameterBV(bvid)
        }.body<ArchiveCoins>()

    override suspend fun hasFavoured(bvid: String): VideoFavoured =
        client.get(BilibiliApi2.VIDEO_FAVOURED) {
            parameter("aid", bvid)
        }.body<VideoFavoured>()

    override suspend fun shortLink(url: String): String =
        client.get(url)
            .body<HttpResponse>()
            .request
            .url
            .toString()

    override suspend fun getView(bvId: String, refresh: Boolean): ViewDetail {
        Napier.d { "获取播放详情 bv=$bvId" }
        return if (refresh) {
            view(bvId)
        } else {
            cacheView.find { it.bvid == bvId } ?: view(bvId)
        }
    }

    override suspend fun getView(aid: Long, refresh: Boolean): ViewDetail {
        val bv = ConvertUtil.Av2Bv(aid)
        Napier.d { "获取播放详情 av=$aid, bv=$bv" }
        return getView(bv)
    }

    private suspend fun view(bvId: String): ViewDetail = client.get(BilibiliApi2.VIEW) {
        parameterBV(bvId)
    }.body<ViewDetail>().apply(cacheView::add)

    override suspend fun 获取视频播放地址(aId: Long, bvId: String, cId: Long): NetworkPlayerPlayUrl {
        Napier.d { "获取播放链接 bv=$bvId, cid=$cId" }
        return cachePlayerPlayUrl.getOrElse("$bvId-$cId") {
            client.wbiGet(BilibiliApi2.PLAYER_PLAY_URL_WBI) {
                parameter("avid", aId)
                parameterBV(bvId)
                parameterCID(cId)
                parameter("qn", 127)
                parameter("fnver", 0)
                parameter("fnval", 4048)
                parameter("fourk", "1")
                parameter("from_client", "BROWSER")
            }.body<NetworkPlayerPlayUrl>()
        }
    }
}
