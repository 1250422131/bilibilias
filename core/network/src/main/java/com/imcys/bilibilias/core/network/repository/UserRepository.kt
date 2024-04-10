package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.user.Card
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.WrapperClient
import com.imcys.bilibilias.core.network.utils.parameterMid
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject

class UserRepository @Inject constructor(
    wrapperClient: WrapperClient
) {
    private val client = wrapperClient.client

    suspend fun 用户名片信息(mid: Long): Card {
        return client.get(BilibiliApi.CARD) {
            parameterMid(mid)
        }.body()
    }
}
