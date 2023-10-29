package com.imcys.bilibilias.common.base.repository.bangumi

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.config.UserInfoRepository
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.flowGet
import com.imcys.bilibilias.common.base.repository.bangumi.model.BangumiFollowList
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BangumiRepository@Inject constructor(private val httpClient: HttpClient) {
    suspend fun bangumiFollowList(pn: Int): Flow<Result<BangumiFollowList>> {
        return httpClient.flowGet(BilibiliApi.bangumiFollowPath) {
            parameter("pn", pn)
            parameter("vmid", UserInfoRepository.mid)
            parameter("ps", PAGE)
            parameter("type", 1)
        }
    }
    companion object {
        const val PAGE = 30
    }
}
