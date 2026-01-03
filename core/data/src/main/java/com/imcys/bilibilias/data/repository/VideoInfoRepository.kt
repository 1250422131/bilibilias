package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.database.dao.BILIUserCookiesDao
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.AppSettings
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.mapData
import com.imcys.bilibilias.network.model.danmuku.DanmakuElem
import com.imcys.bilibilias.network.model.video.BILIDonghuaOgvPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerInfo
import com.imcys.bilibilias.network.model.video.BILIDonghuaPlayerSynthesize
import com.imcys.bilibilias.network.model.video.BILIDonghuaSeasonInfo
import com.imcys.bilibilias.network.model.video.BILIVideoPlayerInfo
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlin.collections.get

class VideoInfoRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val tvAPIService: BILIBILITVAPIService,
    private val usersDataSource: UsersDataSource,
    private val biliUsersDao: BILIUsersDao,
    private val biliUserCookiesDao: BILIUserCookiesDao,
    private val appSettingsRepository: AppSettingsRepository,
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
        fnval: Int = 4048,
        qn: Int = 127,
    ): Flow<NetWorkResult<BILIDonghuaPlayerSynthesize>> {
        val platformType = appSettingsRepository.getVideoParsePlatform()
        val cookieCsrf = biliUserCookiesDao.getBILIUserCookiesByUid(usersDataSource.getUserId())
            .firstOrNull { cookie -> cookie.name == "bili_jct" }?.value
        return when (platformType) {
            AppSettings.VideoParsePlatform.Web,
            AppSettings.VideoParsePlatform.Mobile,
            AppSettings.VideoParsePlatform.UNRECOGNIZED -> {
                webApiService.getDonghuaOgvPlayerInfo(epId, seasonId, qn, fnval, cookieCsrf)
                    .flatMapConcat { ogvInfoResult ->
                        val check =
                            (ogvInfoResult.status == ApiStatus.SUCCESS && ogvInfoResult.responseData?.code != 0) ||
                                    (ogvInfoResult.status == ApiStatus.ERROR)
                        if (check) {
                            // 走新接口，保留类型转换
                            webApiService.getDonghuaPlayerInfo(epId, seasonId, qn, fnval)
                                .map { playerInfoResult ->
                                    if (playerInfoResult.status == ApiStatus.SUCCESS) {
                                        playerInfoResult.mapData { playResult, _ ->
                                            val videoInfo = playResult!!
                                            BILIDonghuaPlayerSynthesize(
                                                isPreview = videoInfo.isPreview == 1L,
                                                acceptFormat = videoInfo.acceptFormat,
                                                dash = videoInfo.dash,
                                                durl = videoInfo.durl,
                                                durls = videoInfo.durls,
                                                quality = videoInfo.quality,
                                                acceptQuality = videoInfo.acceptQuality,
                                                timelength = videoInfo.timelength,
                                                format = videoInfo.format,
                                                videoCodecid = videoInfo.videoCodecid,
                                                supportFormats = videoInfo.supportFormats,
                                                type = videoInfo.type
                                            )
                                        }
                                    } else {
                                        playerInfoResult.mapData { _, _ -> null }
                                    }
                                }
                        } else if (ogvInfoResult.status == ApiStatus.SUCCESS) {
                            flowOf(
                                ogvInfoResult.mapData { ogvResult, _ ->
                                    val videoInfo = ogvResult?.videoInfo!!
                                    BILIDonghuaPlayerSynthesize(
                                        isPreview = ogvResult.playVideoType == "preview",
                                        acceptFormat = videoInfo.acceptFormat,
                                        dash = videoInfo.dash,
                                        durl = videoInfo.durl,
                                        durls = videoInfo.durls,
                                        quality = videoInfo.quality,
                                        acceptQuality = videoInfo.acceptQuality,
                                        timelength = videoInfo.timelength,
                                        format = videoInfo.format,
                                        videoCodecid = videoInfo.videoCodecid,
                                        supportFormats = videoInfo.supportFormats,
                                        type = videoInfo.type
                                    )
                                }
                            )
                        } else {
                            flowOf(ogvInfoResult.mapData { _, _ -> null })
                        }
                    }
            }

            AppSettings.VideoParsePlatform.TV -> {
                var mAid: Long? = null
                var mCid: Long? = null
                val videoView = getDonghuaSeasonViewInfo(epId = epId).last()
                if (videoView.status == ApiStatus.SUCCESS) {
                    val epIndex = videoView.data?.episodes?.indexOfFirst {
                        it.epId == epId
                    }
                    if (epIndex != null && epIndex != -1) {
                        mAid = videoView.data?.episodes?.get(epIndex)?.aid
                        mCid = videoView.data?.episodes?.get(epIndex)?.cid
                    }
                }

                val userId = usersDataSource.getUserId()
                val currentUser = biliUsersDao.getBILIUserByUid(userId)
                var currentAccessToken = currentUser?.accessToken ?: ""
                if (currentUser != null && currentUser.loginPlatform != LoginPlatform.TV) {
                    val user = biliUsersDao.getBILIUserByMidAndPlatform(
                        currentUser.mid,
                        LoginPlatform.TV
                    )
                    currentAccessToken = user?.accessToken ?: ""
                }
                tvAPIService.getDonghuaPlayerInfo(
                    aid = mAid,
                    cid = mCid,
                    fnval = 4048,
                    qn = qn,
                    epId = epId ?: 0L,
                    accessKey = currentAccessToken,
                ).map { playerInfoResult ->
                    if (playerInfoResult.status == ApiStatus.SUCCESS) {
                        playerInfoResult.mapData { playResult, _ ->
                            val videoInfo = playResult!!
                            BILIDonghuaPlayerSynthesize(
                                isPreview = videoInfo.isPreview == 1L,
                                acceptFormat = videoInfo.acceptFormat,
                                dash = videoInfo.dash,
                                durl = videoInfo.durl,
                                durls = videoInfo.durls,
                                quality = videoInfo.quality,
                                acceptQuality = videoInfo.acceptQuality,
                                timelength = videoInfo.timelength,
                                format = videoInfo.format,
                                videoCodecid = videoInfo.videoCodecid,
                                supportFormats = videoInfo.supportFormats,
                                type = videoInfo.type
                            )
                        }
                    } else {
                        playerInfoResult.mapData { _, _ -> null }
                    }
                }
            }
        }
    }


    suspend fun getVideoPlayerInfo(
        cid: Long,
        bvId: String?,
        aid: Long? = null,
        fnval: Int = 4048,
        qn: Int = 127,
        curLanguage: String? = null,
        curProductionType: Int? = null,
    ): Flow<NetWorkResult<BILIVideoPlayerInfo?>> {
        val platformType = appSettingsRepository.getVideoParsePlatform()
        val tryLook = if (usersDataSource.isLogin()) null else "1"
        return when (platformType) {
            AppSettings.VideoParsePlatform.Mobile,
            AppSettings.VideoParsePlatform.UNRECOGNIZED,
            AppSettings.VideoParsePlatform.Web -> {
                webApiService.getVideoPlayerInfo(
                    cid,
                    bvId,
                    aid,
                    fnval,
                    qn,
                    curLanguage,
                    curProductionType,
                    tryLook
                ).map {
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

            AppSettings.VideoParsePlatform.TV -> {
                var mAid = aid
                if (aid == 0L || aid == null) {
                    val videoView = getVideoView(bvId = bvId).last()
                    if (videoView.status == ApiStatus.SUCCESS) {
                        mAid = videoView.data?.aid
                    }
                }

                val userId = usersDataSource.getUserId()
                val currentUser = biliUsersDao.getBILIUserByUid(userId)
                var currentAccessToken = currentUser?.accessToken ?: ""
                if (currentUser != null && currentUser.loginPlatform != LoginPlatform.TV) {
                    val user = biliUsersDao.getBILIUserByMidAndPlatform(
                        currentUser.mid,
                        LoginPlatform.TV
                    )
                    currentAccessToken = user?.accessToken ?: ""
                }
                tvAPIService.getVideoPlayerInfo(
                    cid,
                    mAid,
                    fnval,
                    qn,
                    currentAccessToken
                ).map {
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
    ) = webApiService.getDanmaku(pid, oid, segmentIndex, type)

}