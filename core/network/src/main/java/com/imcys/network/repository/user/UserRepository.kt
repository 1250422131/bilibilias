package com.imcys.network.repository.user

import com.imcys.model.space.SeasonsArchives
import com.imcys.model.space.SeasonsSeriesList
import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.parameterCID
import com.imcys.network.utils.parameterMID
import com.imcys.network.utils.parameterPN
import com.imcys.network.utils.parameterPS
import com.imcys.network.utils.parameterPageNum
import com.imcys.network.utils.parameterPageSize
import com.imcys.network.wbiGet
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val client: HttpClient,
) : IUserDataSources {
    /**
     * 查询用户投稿视频明细
     */
    override suspend fun getSpaceArcSearch(mId: Long, pageNumber: Int): SpaceArcSearch =
        client.wbiGet(BilibiliApi2.WBI_SPACE_ARC_SEARCH) {
            parameterMID(mId)
            parameterPS(30)
            parameterPN(pageNumber)
        }.body<SpaceArcSearch>()

    override suspend fun channelList(mId: Long): SpaceChannelList =
        client.get(BilibiliApi2.SPACE_CHANNEL_LIST) {
            parameterMID(mId)
        }.body<SpaceChannelList>()

    override suspend fun channelVideo(
        mId: Long,
        channelId: Long,
        pn: Int,
        ps: Int
    ): SpaceChannelVideo = client.get(BilibiliApi2.SPACE_CHANNEL_VIDEO) {
        parameterMID(mId)
        parameterCID(channelId)
        parameterPN(pn)
        parameterPS(ps)
    }.body<SpaceChannelVideo>()

    override suspend fun seasonsSeriesList(
        mId: Long,
        pageNumber: Int,
        pageSize: Int
    ): SeasonsSeriesList = client.get(BilibiliApi2.SPACE_SEASONS_SERIES_LIST) {
        parameterMID(mId)
        parameterPageNum(pageNumber)
        parameterPageSize(20)
    }.body()

    override suspend fun seasonsArchivesList(
        mId: Long,
        seasonId: Long,
        total: Int
    ): ImmutableList<SeasonsArchives> {
        val size = 30
        var result = persistentListOf<SeasonsArchives>()
        for (i in 1..total / size + 1) {
            result += client.get(BilibiliApi2.SPACE_SEASONS_ARCHIVES_LIST) {
                parameterMID(mId)
                parameter("sort_reverse", false)
                parameter("season_id", seasonId)
                parameterPageNum(i)
                parameterPageSize(size)
            }.body<SeasonsArchives>()
        }
        return result
    }
}
