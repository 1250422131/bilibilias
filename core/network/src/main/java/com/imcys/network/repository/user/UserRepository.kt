package com.imcys.network.repository.user

import com.imcys.model.space.ChannelCollectionDetail
import com.imcys.model.space.ChannelSeriesDetail
import com.imcys.model.space.ChannelsWithArchives
import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.utils.PAGE_SIZE_20
import com.imcys.network.utils.PAGE_SIZE_30
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
            parameterPS(PAGE_SIZE_30)
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

    override suspend fun getChannelsWithArchives(
        mId: Long,
        pageNumber: Int
    ): ChannelsWithArchives = client.wbiGet(BilibiliApi2.SPACE_SEASONS_SERIES_LIST) {
        parameterMID(mId)
        parameterPageNum(pageNumber)
        parameterPageSize(PAGE_SIZE_20)
    }.body()

    override suspend fun getChannelCollectionDetail(
        mId: Long,
        seasonId: Long,
        pageNum: Int
    ): ChannelCollectionDetail {
        return client.get(BilibiliApi2.SPACE_SEASONS_ARCHIVES_LIST) {
            parameterMID(mId)
            parameter("sort_reverse", false)
            parameter("season_id", seasonId)
            parameterPageNum(pageNum)
            parameterPageSize(PAGE_SIZE_30)
        }.body()
    }

    override suspend fun getChannelSeriesDetail(seriesId: Long): ChannelSeriesDetail {
        return client.get(BilibiliApi2.SERIES_SERIES) {
            parameter("series_id", seriesId)
        }.body()
    }
}
