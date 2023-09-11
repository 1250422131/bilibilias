package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.model.UserSpaceInformation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiRepository: WbiKeyRepository
) {
    suspend fun getUserSpaceDetails(mid: Long): UserSpaceInformation {
        val params = wbiRepository.getUserNavToken(listOf("mid" to mid.toString()))
        val information = httpClient.get(BilibiliApi.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body<UserSpaceInformation>()
        return information
    }
}
