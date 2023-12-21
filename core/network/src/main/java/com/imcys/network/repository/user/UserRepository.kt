package com.imcys.network.repository.user

import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.datastore.fastkv.WbiKeyStorage
import com.imcys.model.UserCardBean
import com.imcys.model.UserSpaceInformation
import com.imcys.model.space.SpaceArcSearch
import com.imcys.network.api.BilibiliApi2
import com.imcys.network.repository.Parameter
import com.imcys.network.repository.wbi.WbiKeyRepository
import com.imcys.network.utils.parameterMID
import com.imcys.network.utils.parameterPN
import com.imcys.network.utils.parameterPS
import com.imcys.network.wbiGet
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
    private val client: HttpClient,
    private val wbiRepository: WbiKeyRepository,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val wbiKeyStorage: WbiKeyStorage
) : IUserDataSources {
    /** 查询用户投稿视频明细 */
    override suspend fun getSpaceArcSearch(mid: Long, pageNumber: Int): SpaceArcSearch =
        withContext(ioDispatcher) {
            client.wbiGet(BilibiliApi2.WBI_SPACE_ARC_SEARCH) {
                parameter("mid", mid)
                parameterPS(30)
                parameterPN(pageNumber)
            }.body<SpaceArcSearch>()
        }

    /** todo 也许可以返回文本，来进行解析 */
    suspend fun get用户名片信息(mid: Long): UserCardBean = withContext(ioDispatcher) {
        client.get(BilibiliApi2.getUserCardPath) {
            parameterMID(mid)
        }.body()
    }


    suspend fun getUserSpaceDetails(mid: Long): UserSpaceInformation = withContext(ioDispatcher) {
        val params = wbiRepository.getUserNavToken(listOf(Parameter("mid", mid.toString())))
        client.get(BilibiliApi2.userSpaceDetails) {
            params.forEach { (k, v) ->
                parameter(k, v)
            }
        }.body()
    }
}
