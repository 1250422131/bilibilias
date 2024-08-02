package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.space.FavouredFolder
import com.imcys.bilibilias.core.model.space.SpaceArcSearch
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.requireWbi
import com.imcys.bilibilias.core.network.utils.parameterMid
import com.imcys.bilibilias.core.network.utils.parameterUpMid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import javax.inject.Inject

class UserSpaceRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun 查询用户创建的视频收藏夹(mid: Long): FavouredFolder {
        return client.get(BilibiliApi.FAVOURED_FOLDER_ALL) {
            parameterUpMid(mid)
        }.body()
    }

    suspend fun 查询用户投稿视频(mid: Long, next: Int, size: Int): SpaceArcSearch {
        return client.get(BilibiliApi.SPACE_ARC_SEARCH) {
            attributes.put(requireWbi, true)
            header(HttpHeaders.Referrer, "https://www.bilibili.com")
            parameterMid(mid)
            parameter("pn", next)
            parameter("ps", size.coerceAtMost(30))
        }.body()
    }
}
