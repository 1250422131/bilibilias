package com.imcys.network.repository.user

import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo

interface IUserDataSources {
    suspend fun getSpaceArcSearch(mid: Long, pageNumber: Int): SpaceArcSearch
    suspend fun channelList(mId: Long): SpaceChannelList
    suspend fun channelVideo(mId: Long, channelId: Long, pn: Int, ps: Int = 100): SpaceChannelVideo
    /**
     * todo 新接口
     * 获取up合集：https://api.bilibili.com/x/polymer/space/seasons_series_list?mid=8047632&page_num=1&page_size=20
     * 获取合集内容https://api.bilibili.com/x/polymer/space/seasons_archives_list?mid=8047632&season_id=413472&sort_reverse=false&page_num=1&page_size=30
     * mid是up uid, season_id是合集的id
     */
}
