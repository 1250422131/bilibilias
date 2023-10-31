package com.imcys.network

import com.imcys.model.UserCardBean
import com.imcys.model.UserSpaceInformation
import com.imcys.network.api.BilibiliApi2
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
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
        httpClient.safeGet<UserCardBean>(BilibiliApi2.getUserCardPath) {
            parameter("mid", mid)
        }

    suspend fun getUserSpaceDetails(mid: Long): com.imcys.common.utils.Result<UserSpaceInformation> {
        val params = wbiRepository.getUserNavToken(listOf("mid" to mid.toString()))
        val information = httpClient.safeGet<UserSpaceInformation>(BilibiliApi2.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }
        return information
    }
}
