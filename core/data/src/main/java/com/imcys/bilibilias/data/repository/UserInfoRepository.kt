package com.imcys.bilibilias.data.repository

import com.imcys.bilibilias.data.model.BILISpaceArchiveModel
import com.imcys.bilibilias.data.model.BILIUserStatModel
import com.imcys.bilibilias.data.model.user.BILIUserHistoryPlayModel
import com.imcys.bilibilias.database.dao.BILIUsersDao
import com.imcys.bilibilias.database.entity.BILIUsersEntity
import com.imcys.bilibilias.database.entity.LoginPlatform
import com.imcys.bilibilias.datastore.source.UsersDataSource
import com.imcys.bilibilias.network.FlowNetWorkResult
import com.imcys.bilibilias.network.mapData
import com.imcys.bilibilias.network.model.user.BILIUserVideoLikeInfo
import com.imcys.bilibilias.network.service.BILIBILITVAPIService
import com.imcys.bilibilias.network.service.BILIBILIWebAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlin.math.ceil

/**
 * 用户信息仓库
 * 目前只有B站的，后续增加AcFun和AS账户
 */
class UserInfoRepository(
    private val webApiService: BILIBILIWebAPIService,
    private val tvAPIService: BILIBILITVAPIService,
    private val biliUsersDao: BILIUsersDao,
    private val usersDataSource: UsersDataSource
) {


    suspend fun isLogin() = usersDataSource.getUserId() != 0L

    // 获取当前用户信息
    suspend fun getCurrentUser(): BILIUsersEntity? {
        return biliUsersDao.getBILIUserByUid(usersDataSource.getUserId())
    }

    suspend fun getUserPageInfo(mid: Long) = webApiService.getUserAccInfo(mid)

    suspend fun getUserStatInfo(mid: Long): Flow<BILIUserStatModel> {
        return combine(
            webApiService.getSpaceUpStat(mid),
            webApiService.getRelationStat(mid),
        ) { result1, result2 ->
            BILIUserStatModel(result1, result2)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getBILIUserByUid(userId: Long? = null) =
        biliUsersDao.getBILIUserByUid(userId ?: usersDataSource.getUserId())

    suspend fun deleteBILIUserByUid(userId: Long) = biliUsersDao.deleteBILIUserByUid(userId)

    suspend fun getBILIUserListByMid(mid: Long) = biliUsersDao.getBILIUserListByMid(mid)
    suspend fun getSpaceArchiveInfo(
        mid: Long,
        pn: Int = 1,
        ps: Int = 2,
        keyword: String? = null,
    ): FlowNetWorkResult<BILISpaceArchiveModel> {
        val userInfo = getBILIUserByUid()
        return when (userInfo?.loginPlatform ?: LoginPlatform.WEB) {
            LoginPlatform.WEB -> {
                webApiService.getSpaceArchiveInfo(mid, pn, ps, keyword).map { networkResult ->
                    networkResult.mapData { archiveInfo, apiResponse ->
                        val allCount = archiveInfo?.page?.count ?: 0
                        BILISpaceArchiveModel(
                            list = archiveInfo?.list?.vlist?.map {
                                BILISpaceArchiveModel.Item(
                                    title = it.title,
                                    bvid = it.bvid,
                                    author = it.author,
                                    aid = it.aid,
                                    play = it.play,
                                    description = it.description,
                                    seasonId = it.seasonId,
                                    pic = it.pic,
                                    attribute = it.attribute,
                                    length = it.length,
                                    comment = it.comment,
                                    danmu = it.videoReview
                                )
                            } ?: emptyList(),
                            page = BILISpaceArchiveModel.Page(
                                count = allCount,
                                hasNext = ceil(allCount * 1.0 / ps) > pn,
                                hasPrev = pn > 1
                            )
                        )
                    }
                }
            }

            LoginPlatform.MOBILE,
            LoginPlatform.TV -> {
                webApiService.getSpaceArchiveInfo(mid, pn, ps, keyword).map { networkResult ->
                    networkResult.mapData { archiveInfo, apiResponse ->
                        val allCount = archiveInfo?.page?.count ?: 0
                        BILISpaceArchiveModel(
                            list = archiveInfo?.list?.vlist?.map {
                                BILISpaceArchiveModel.Item(
                                    title = it.title,
                                    bvid = it.bvid,
                                    author = it.author,
                                    aid = it.aid,
                                    play = it.play,
                                    description = it.description,
                                    seasonId = it.seasonId,
                                    pic = it.pic,
                                    attribute = it.attribute,
                                    length = it.length,
                                    comment = it.comment,
                                    danmu = it.videoReview
                                )
                            } ?: emptyList(),
                            page = BILISpaceArchiveModel.Page(
                                count = allCount,
                                hasNext = ceil(allCount * 1.0 / ps) > pn,
                                hasPrev = pn > 1
                            )
                        )
                    }
                }
            }
        }
    }


    suspend fun getBangumiFollowInfo(
        vmid: Long,
        type: Int = 1,
        pn: Int = 1,
        ps: Int = 20
    ) = webApiService.getBangumiFollowInfo(vmid, type, pn, ps)

    suspend fun getFolderList(mid: Long) = webApiService.getFolderList(mid)

    suspend fun getFolderFavList(
        mediaId: Long,
        pn: Int = 1,
        ps: Int = 40
    ) = webApiService.getFolderFavList(mediaId, pn, ps)

    suspend fun getLikeVideoList(
        mid: Long,
    ) = webApiService.getLikeVideoList(mid)

    suspend fun getCoinVideoList(
        mid: Long,
    ) = webApiService.getCoinVideoList(mid).map { networkResult ->
        networkResult.mapData { coinInfo, apiResponse ->
            BILIUserVideoLikeInfo(
                list = coinInfo ?: emptyList()
            )
        }
    }

    suspend fun getHistoryCursor(
        max: Long = 0L,
        viewAt: Long = 0L,
        ps: Int = 20,
        type: String = "archive",
    ) = webApiService.getHistoryCursor(max, viewAt, ps, type).map { networkResult ->
        networkResult.mapData { historyInfo, apiResponse ->
            historyInfo?.list?.map { info ->
                BILIUserHistoryPlayModel(
                    longTitle = info.longTitle,
                    title = info.title,
                    cover = info.cover,
                    history = info.history,
                    showTitle = info.showTitle,
                    duration = info.duration,
                    tagName = info.tagName,
                    progress = info.progress,
                    authorMid = info.authorMid,
                    authorName = info.authorName,
                    max = historyInfo.cursor.max,
                    viewAt = historyInfo.cursor.viewAt
                )
            }

        }
    }

    suspend fun logout(biliJct: String) = webApiService.logout(biliJct)

}