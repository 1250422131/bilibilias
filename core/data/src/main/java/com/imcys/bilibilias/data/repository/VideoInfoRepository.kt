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
}