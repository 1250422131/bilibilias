package com.imcys.network.repository.user

import com.imcys.model.space.ChannelCollectionDetail
import com.imcys.model.space.ChannelSeriesDetail
import com.imcys.model.space.ChannelsWithArchives
import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo

interface IUserDataSources {
    suspend fun getSpaceArcSearch(mId: Long, pageNumber: Int): SpaceArcSearch
    suspend fun channelList(mId: Long): SpaceChannelList
    suspend fun channelVideo(mId: Long, channelId: Long, pn: Int, ps: Int = 100): SpaceChannelVideo
    suspend fun getChannelsWithArchives(mId: Long, pageNumber: Int): ChannelsWithArchives

    suspend fun getChannelCollectionDetail(
        mId: Long,
        seasonId: Long,
        pageNum: Int
    ): ChannelCollectionDetail

    suspend fun getChannelSeriesDetail(seriesId: Long): ChannelSeriesDetail
}
