package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.model.danmuku.DanmakuElem
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlin.collections.get

class VideoInfoRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val tvAPIService: BILIBILITVAPIService,
    private val usersDataSource: UsersDataSource,
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao
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
        qn: Int = 12240,
        fnval: Int = 127,
    ) = webApiService.getDonghuaPlayerInfo(epId, seasonId, qn, fnval)


    suspend fun getVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
        fnval: Int = 4048,
        qn: Int = 127,
    ): Flow<NetWorkResult<BILIVideoPlayerInfo?>> {
        val tryLook = if (usersDataSource.isLogin()) null else "1"
        return webApiService.getVideoPlayerInfo(cid, bvId, aid, fnval, qn, tryLook).map {
            if (it.status == ApiStatus.SUCCESS) {
                // 杜比
                it.data?.dash?.dolby?.audio?.let { dolbyList ->
                    if (dolbyList.isNotEmpty()) {
                        it.data?.dash?.audio?.add(0, dolbyList[0])
                    }
                }

                // Hi—Res
                it.data?.dash?.flac?.audio?.let { flac ->
                    it.data?.dash?.audio?.add(0, flac)
                }
            }
            it
        }
    }

    /**
     * 获取额外的播放信息
     */
    suspend fun getVideoPlayerInfoV2(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
    ) = webApiService.getVideoPlayerInfoV2(cid, bvId, aid)

    suspend fun getVideoCCInfo(url: String) = webApiService.getVideoCCInfo(url)

    /**
     * 互动视频-获取剧集信息
     */
    suspend fun getSteinEdgeInfoV2(
        bvId: String? = null,
        aid: String? = null,
        graphVersion: Long = 0,
        edgeId: Long? = 0
    ) = webApiService.getSteinEdgeInfoV2(bvId, aid, graphVersion, edgeId)

    suspend fun getDanmaku(
        pid: Long? = null,
        oid: Long,
        segmentIndex: Int,
        type: Int = 1
    ) = webApiService.getDanmaku(pid, oid,segmentIndex, type)

}