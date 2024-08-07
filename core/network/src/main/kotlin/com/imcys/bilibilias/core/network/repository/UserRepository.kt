package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.model.user.Card
import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.utils.parameterMid
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
@Suppress("ktlint:standard:function-naming")
class UserRepository @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun 用户名片信息(mid: Long): Card {
        Napier.d { mid.toString() }
        return client.get(BilibiliApi.CARD) {
            parameterMid(mid)
        }.body()
    }
}
