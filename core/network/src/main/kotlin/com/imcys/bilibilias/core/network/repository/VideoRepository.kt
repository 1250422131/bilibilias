package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.Response
import com.imcys.bilibilias.core.model.video.ArchiveCoins
import com.imcys.bilibilias.core.model.video.ArchiveFavoured
import com.imcys.bilibilias.core.model.video.ArchiveLike
import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.requireCSRF
import com.imcys.bilibilias.core.network.di.requireWbi
import com.imcys.bilibilias.core.network.utils.parameterAVid
import com.imcys.bilibilias.core.network.utils.parameterAid
import com.imcys.bilibilias.core.network.utils.parameterBVid
import com.imcys.bilibilias.core.network.utils.parameterCid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.request
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun shortLink(url: String): String {
        return client.get(url)
            .request
            .url
            .toString()
    }
    suspend fun videoStreamingURL(aid: Long, bvid: String, cid: Long): VideoStreamUrl {
        return client.get("x/player/wbi/playurl") {
            attributes.put(requireWbi, true)
            parameterAVid(aid)
            parameterBVid(bvid)
            parameterCid(cid)
            parameter("qn", 127)
            parameter("fnver", 0)
            parameter("fnval", 4048)
            parameter("fourk", 1)
            parameter("from_client", "BROWSER")
        }.body()
    }

    suspend fun 获取视频详细信息(bvid: String): ViewDetail {
        return client.get(BilibiliApi.VIEW) {
            parameterBVid(bvid)
        }.body()
    }

    /**
     * 检验是否点赞
     */
    suspend fun getArchiveLike(bvid: String): Boolean {
        val response =
            client.get(BilibiliApi.ARCHIVE_HAS_LIKE) { parameterBVid(bvid) }.body<ArchiveLike>()
        return response.data == 1
    }

    /**
     * 检验投币情况
     */
    suspend fun getArchiveCoins(bvid: String): Boolean {
        val response = client.get(BilibiliApi.ARCHIVE_COINS) {
            parameterBVid(bvid)
        }.body<ArchiveCoins>()
        return response.hasCoins
    }

    /**
     * 收藏检验
     */
    suspend fun getArchiveFavoured(bvid: String): Boolean {
        val response = client.get(BilibiliApi.ARCHIVE_FAVOURED) {
            parameterAid(bvid)
        }.body<ArchiveFavoured>()
        return response.favoured
    }

    suspend fun 点赞视频(hasLike: Boolean, bvid: String): Response {
        return client.post(BilibiliApi.ARCHIVE_LIKE) {
            attributes.put(requireCSRF, true)
            parameterBVid(bvid)
            parameter("like", if (hasLike) 2 else 1)
        }.body<Response>()
    }

    suspend fun 投币视频(bvid: String): Response {
        return client.post(BilibiliApi.COIN_ADD) {
            attributes.put(requireCSRF, true)
            parameterBVid(bvid)
            parameter("select_like", 1)
            parameter("multiply", 2)
        }.body<Response>()
    }

    suspend fun 收藏视频(aid: Long, addIds: String, delIds: String = ""): Response {
        return client.get(BilibiliApi.ARCHIVE_RESOURCE_FAVOURED) {
            attributes.put(requireCSRF, true)
            parameter("rid", aid)
            parameter("add_media_ids", addIds)
            parameter("type", "2")
        }.body()
    }
}
