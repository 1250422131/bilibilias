package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.space.FavouredFolder
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.utils.parameterUpMid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class UserSpaceRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun 查询用户创建的视频收藏夹(mid: Long): FavouredFolder {
        return client.get(BilibiliApi.FAVOURED_FOLDER_ALL) {
            parameterUpMid(mid)
        }.body()
    }
}
