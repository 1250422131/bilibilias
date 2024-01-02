package com.imcys.network.repository.user

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.UserCardBean
import com.imcys.model.UserSpaceInformation
import com.imcys.model.space.SeasonsArchivesList
import com.imcys.model.space.SeasonsSeriesList
import com.imcys.model.space.SpaceArcSearch
import com.imcys.model.space.SpaceChannelList
import com.imcys.model.space.SpaceChannelVideo
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.repository.Parameter
import com.imcys.network.repository.wbi.WbiKeyRepository
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val client: HttpClient,
    private val wbiRepository: WbiKeyRepository,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : IUserDataSources {
    /**
     * 查询用户投稿视频明细
     */
    override suspend fun getSpaceArcSearch(mId: Long, pageNumber: Int): SpaceArcSearch =
        withContext(ioDispatcher) {
            client.wbiGet(BilibiliApi2.WBI_SPACE_ARC_SEARCH) {
                parameterMID(mId)
                parameterPS(30)
                parameterPN(pageNumber)
            }.body<SpaceArcSearch>()
        }

    override suspend fun channelList(mId: Long): SpaceChannelList = withContext(ioDispatcher) {
        client.get(BilibiliApi2.SPACE_CHANNEL_LIST) {
            parameterMID(mId)
        }.body<SpaceChannelList>()
    }

    override suspend fun channelVideo(
        mId: Long,
        channelId: Long,
        pn: Int,
        ps: Int
    ): SpaceChannelVideo =
        withContext(ioDispatcher) {
            client.get(BilibiliApi2.SPACE_CHANNEL_VIDEO) {
                parameterMID(mId)
                parameterCID(channelId)
                parameterPN(pn)
                parameterPS(ps)
            }.body<SpaceChannelVideo>()
        }

    override suspend fun seasonsSeriesList(
        mId: Long,
        pageNumber: Int,
        pageSize: Int
    ): SeasonsSeriesList = withContext(ioDispatcher) {
        client.get(BilibiliApi2.SPACE_SEASONS_SERIES_LIST) {
            parameterMID(mId)
            parameterPageNum(pageNumber)
            parameterPageSize(20)
        }.body()
    }

    override suspend fun seasonsArchivesList(
        mId: Long,
        seasonId: Long,
        pageNumber: Int,
        sort: Boolean,
        pageSize: Int
    ): SeasonsArchivesList = withContext(ioDispatcher) {
        client.get(BilibiliApi2.SPACE_SEASONS_ARCHIVES_LIST) {
            parameterMID(mId)
            parameter("sort_reverse", sort)
            parameter("season_id", seasonId)
            parameterPageNum(pageNumber)
            parameterPageSize(pageSize)
        }.body()
    }

    /**
     * todo 也许可以返回文本，来进行解析
     */
    suspend fun get用户名片信息(mid: Long): UserCardBean = withContext(ioDispatcher) {
        client.get(BilibiliApi2.getUserCardPath) {
            parameterMID(mid)
        }.body()
    }

    suspend fun getUserSpaceDetails(mid: Long): UserSpaceInformation = withContext(ioDispatcher) {
        val params = wbiRepository.getUserNavToken(listOf(Parameter("mid", mid.toString())))
        client.get(BilibiliApi2.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body()
    }
}
