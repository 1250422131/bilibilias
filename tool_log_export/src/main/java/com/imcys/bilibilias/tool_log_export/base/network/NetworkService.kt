package com.imcys.bilibilias.tool_log_export.base.network

import com.imcys.bilibilias.common.base.api.BilibiliApi
import com.imcys.bilibilias.common.base.model.common.BangumiFollowList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkService @Inject constructor(
    private val httpClient: HttpClient
) {
    private val ioDispatcher = Dispatchers.IO

    suspend fun getBangumiFollow(vmid: Long, type: Int, pn: Int, ps: Int): BangumiFollowList =
        runCatchingOnWithContextIo {
            httpClient.get(BilibiliApi.bangumiFollowPath) {
                parameter("vmid", vmid)
                parameter("type", type)
                parameter("pn", pn)
                parameter("ps", ps)
            }
                .body()
        }

    private suspend inline fun <reified T> runCatchingOnWithContextIo(
        noinline block: suspend CoroutineScope.() -> T
    ): T {
        return runCatching {
            withContext(ioDispatcher, block)
        }.getOrElse {
            val clazz = T::class
            val constructors = clazz.constructors
            val emptyConstructor = constructors.find { parameter -> parameter.parameters.isEmpty() }
            emptyConstructor?.call() as T ?: throw it
        }
    }
}