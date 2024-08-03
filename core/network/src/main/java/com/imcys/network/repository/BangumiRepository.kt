package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.common.utils.Result
import com.imcys.model.BangumiFollowList
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.flowGet
import com.imcys.network.utils.parameterPN
import com.imcys.network.utils.parameterPS
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BangumiRepository @Inject constructor(
    private val httpClient: HttpClient,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun bangumiFollowList(pn: Int): Flow<Result<BangumiFollowList>> {
        return httpClient.flowGet(BilibiliApi2.bangumiFollowPath) {
            parameter("vmid", TODO())
            parameterPN(pn)
            parameterPS(PAGE)
            parameter("type", 1)
        }
    }

    companion object {
        const val PAGE = 30
    }
}