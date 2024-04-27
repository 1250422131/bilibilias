package com.imcys.bilibilias.common.network.danmaku

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.network.base.ResBean
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanmakuRepository @Inject constructor(private val http: HttpClient) {
    suspend fun getCideoInfoV2(avid: Long, cid: Long): Flow<Result<ResBean<VideoInfoV2>>> =
        http.safeGet(BilibiliApi.videoInfoV2) {
            parameter("aid", avid)
            parameter("cid", cid)
        }
}
