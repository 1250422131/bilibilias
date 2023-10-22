package com.imcys.bilibilias.common.base.repository

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.safeGet
import com.imcys.bilibilias.common.base.model.UserCardBean
import com.imcys.bilibilias.common.base.model.UserSpaceInformation
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.http.parametersOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiRepository: WbiKeyRepository
) {
    /**
     * todo 也许可以返回文本，来进行解析
     */
    suspend fun get用户名片信息(mid: Long) =
        httpClient.safeGet<UserCardBean>(BilibiliApi.getUserCardPath) {
            parameter("mid", mid)
        }

    suspend fun getUserSpaceDetails(mid: Long): Result<UserSpaceInformation> {
        val params = wbiRepository.getUserNavToken(listOf("mid" to mid.toString()))
        val information = httpClient.safeGet<UserSpaceInformation>(BilibiliApi.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }
        return information
    }
}
