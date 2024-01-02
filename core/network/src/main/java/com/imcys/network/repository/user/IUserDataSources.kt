package com.imcys.network.repository.user

import com.imcys.model.space.SeasonsArchivesList
import com.imcys.model.space.SeasonsSeriesList
import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo

interface IUserDataSources {
    suspend fun getSpaceArcSearch(mId: Long, pageNumber: Int): SpaceArcSearch
    suspend fun channelList(mId: Long): SpaceChannelList
    suspend fun channelVideo(mId: Long, channelId: Long, pn: Int, ps: Int = 100): SpaceChannelVideo

    /**
     * https://api.bilibili.com/x/polymer/space/seasons_series_list?mid=8047632&page_num=1&page_size=10
     * https://api.bilibili.com/x/polymer/space/seasons_series_list?mid=2379178&page_num=1&page_size=20
     * 获取up合集
     */
    suspend fun seasonsSeriesList(mId: Long, pageNumber: Int, pageSize: Int = 20): SeasonsSeriesList

    /**
     * https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=8047632&season_id=413472&sort_reverse=false&page_num=1&page_size=30
     * 获取合集内容
     */
    suspend fun seasonsArchivesList(
        mId: Long,
        seasonId: Long,
        pageNumber: Int,
        sort: Boolean = false,
        pageSize: Int = 30
    ): SeasonsArchivesList
}
