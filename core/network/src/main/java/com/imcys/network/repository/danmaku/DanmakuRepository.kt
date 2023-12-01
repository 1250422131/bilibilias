package com.imcys.network.repository.danmaku

import com.imcys.bilibilias.dm.DmSegMobileReply
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.network.repository.WbiKeyRepository
import com.imcys.network.utils.parameterList
import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.readBytes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanmakuRepository @Inject constructor(
    private val client: HttpClient,
    private val wbiKeyRepository: WbiKeyRepository,
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : IDanmakuDataSources {
    override suspend fun xml(cid: Long): ByteArray = withContext(ioDispatcher) {
        client.get(DanmakuXml(cid)) {
            parameter("oid", cid)
        }
            .bodyAsChannel()
            .readRemaining()
            .readBytes()
    }

    override suspend fun proto(cid: Long, index: Int, type: Int): DmSegMobileReply =
        withContext(ioDispatcher) {
            val bytes = client.get(DanmakuProto.Web(cid, type, index)) {
                parameter("oid", cid)
                parameter("type", type)
                parameter("segment_index", index)
            }.bodyAsChannel()
                .readRemaining()
                .readBytes()
            DmSegMobileReply.ADAPTER.decode(bytes)
        }

    override suspend fun protoWbi(cid: Long, index: Int, type: Int): DmSegMobileReply =
        withContext(ioDispatcher) {
            val wbiWeb = DanmakuProto.WbiWeb(cid, type, index)
            val parameter = wbiKeyRepository.getUserNavToken(wbiWeb.buildParameter())
            val bytes = client.get(wbiWeb) {
                parameterList(parameter)
            }.bodyAsChannel()
                .readRemaining()
                .readBytes()
            DmSegMobileReply.ADAPTER.decode(bytes)
        }
}
