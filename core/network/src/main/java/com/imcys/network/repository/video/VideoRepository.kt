package com.imcys.network.repository.video

import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import com.imcys.common.utils.ConvertUtil
import com.imcys.model.PgcPlayUrl
import com.imcys.model.PgcViewSeason
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails
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
    private val cacheViewDetail = ArraySet<VideoDetails>()
    private val cachePlayerPlayUrl = ArrayMap<String, PlayerInfo>()

    /**
     * /pgc/player/web/v2/playurl? support_multi_audio=true&
     * avid=495494010& cid=1385942568& qn=116& fnver=0& fnval=4048&
     * fourk=1& gaia_source=& from_client=BROWSER& ep_id=809818&
     * session=5bafa0564461e25b2d751bd2eb1d8816& drm_tech_type=2
     */
    override suspend fun getPgcPlayUrl(epID: Long, aId: Long, cId: Long): PgcPlayUrl {
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

    override suspend fun getPgcViewSeason(epId: String): PgcViewSeason {
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

    override suspend fun getDetail(bvid: String): VideoDetails {
        Napier.d { "获取播放详情 bv=$bvid" }
        return client.get(BilibiliApi2.VIEW_DETAIL) {
            parameterBV(bvid)
        }.body<VideoDetails>()
    }

    override suspend fun getDetail(aid: Long): VideoDetails {
        Napier.d { "获取播放详情 av=$aid" }
        val detail = cacheViewDetail.find { it.aid == aid }
        return if (detail == null) {
            val bv = ConvertUtil.Av2Bv(aid)
            val detail1 = getDetail(bv)
            cacheViewDetail.add(detail1)
            detail1
        } else {
            detail
        }
    }

    /**
     * /x/player/wbi/playurl? avid=798099135& bvid=BV1Uy4y1S7Eq&
     * cid=265272502& qn=116& fnver=0& fnval=4048& fourk=1& gaia_source=&
     * from_client=BROWSER& session=9db28d7eb6388c1b0f03a56661705fd6&
     * voice_balance=1& web_location=1315873&
     * w_rid=996ed7de5ee9d46270df3826f98c8cd1&wts=1705459810
     */
    override suspend fun getPlayerPlayUrl(bvid: String, cid: Long): PlayerInfo {
        Napier.d { "获取播放链接 bv=$bvid, cid=$cid" }
        return cachePlayerPlayUrl.getOrElse("$bvid-$cid") {
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
