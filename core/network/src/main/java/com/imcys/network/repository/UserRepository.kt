package com.imcys.network.repository

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.model.UserCardBean
import com.imcys.model.UserSpaceInformation
import com.imcys.network.api.BilibiliApi2
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
class UserRepository @Inject constructor(
    private val httpClient: HttpClient,
    private val wbiRepository: WbiKeyRepository,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * todo 也许可以返回文本，来进行解析
     */
    suspend fun get用户名片信息(mid: Long): UserCardBean = withContext(ioDispatcher) {
        httpClient.get(BilibiliApi2.getUserCardPath) {
            parameterMID(mid)
        }.body()
    }


    suspend fun getUserSpaceDetails(mid: Long): UserSpaceInformation = withContext(ioDispatcher) {
        val params = wbiRepository.getUserNavToken(listOf("mid" to mid.toString()))
        httpClient.get(BilibiliApi2.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body()
    }
}
