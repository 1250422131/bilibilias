package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.SpaceChannelVideo
import com.imcys.network.utils.parameterMID
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpaceRepository @Inject constructor(
    private val httpClient: HttpClient,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) {
    /**
     * 查询用户频道中的视频
     * curl -G 'https://api.bilibili.com/x/space/channel/video' \
     * --data-urlencode 'mid=53456' \
     * --data-urlencode 'cid=170' \
     */
    suspend fun queryUserChannelsVideos(mid: Long, cid: Long): SpaceChannelVideo = withContext(ioDispatcher) {
        httpClient.get("x/space/channel/video") {
            parameterMID(mid)
            parameter("cid", cid)
        }.body()
    }
}