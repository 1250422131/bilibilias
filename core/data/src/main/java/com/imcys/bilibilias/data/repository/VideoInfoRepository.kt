package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService

class VideoInfoRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val tvAPIService: BILIBILITVAPIService,
    private val usersDataSource: UsersDataSource
) {

    suspend fun getVideoView(
        bvId: String? = null,
        aid: String? = null,
    ) = webApiService.getVideoView(bvId, aid)

    suspend fun shortLink(
        url: String
    ) = webApiService.shortLink(url)

    suspend fun getDonghuaSeasonViewInfo(
        epId: Long? = null,
        seasonId: Long? = null,
    ) = webApiService.getDonghuaSeasonViewInfo(epId, seasonId)

    suspend fun getDonghuaPlayerInfo(
        epId: Long?,
        seasonId: Long?,
        qn: Int = 30280,
        fnval: Int = 80,
    ) = webApiService.getDonghuaPlayerInfo(epId, seasonId, qn, fnval)


    suspend fun getVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
        fnval: Int = 4048,
        qn: Int = 116,
    ) = webApiService.getVideoPlayerInfo(cid, bvId, aid, fnval, qn)
}