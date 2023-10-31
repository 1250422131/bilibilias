package com.imcys.network

import com.imcys.common.utils.Result
import com.imcys.model.BangumiFollowList
import com.imcys.network.api.BilibiliApi2
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BangumiRepository@Inject constructor(private val httpClient: HttpClient) {
    suspend fun bangumiFollowList(pn: Int):Flow<Result<BangumiFollowList>> {
        return httpClient.flowGet(BilibiliApi2.bangumiFollowPath) {
            parameter("pn", pn)
            parameter("vmid", TODO())
            parameter("ps", PAGE)
            parameter("type", 1)
        }
    }
    companion object {
        const val PAGE = 30
    }
}