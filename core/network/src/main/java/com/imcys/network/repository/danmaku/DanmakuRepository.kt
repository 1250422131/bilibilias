package com.imcys.network.repository.danmaku

import com.imcys.bilibilias.dm.DmSegMobileReply
import com.imcys.common.di.AsDispatchers
import com.imcys.common.di.Dispatcher
import com.imcys.datastore.fastkv.WbiKeyStorage
import com.imcys.network.wbiGet
import io.ktor.client.HttpClient
import io.ktor.client.request.get
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
    @Dispatcher(AsDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val wbiKeyStorage: WbiKeyStorage
) : IDanmakuDataSources {
    override suspend fun xml(cid: Long): ByteArray = withContext(ioDispatcher) {
        client.get("x/v1/dm/list.so") {
            parameter("oid", cid)
        }
            .bodyAsChannel()
            .readRemaining()
            .readBytes()
    }

    @Deprecated("网页端接口大多需要wbi")
    override suspend fun proto(cid: Long, index: Int, type: Int): DmSegMobileReply =
        withContext(ioDispatcher) {
            val bytes = client.get("DanmakuProto.Web(cid, type, index)") {
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
            val bytes = client.wbiGet("x/v2/dm/wbi/web/seg.so") {
                parameter("oid", cid)
                parameter("type", type)
                parameter("segment_index", index)
            }.bodyAsChannel()
                .readRemaining()
                .readBytes()
            DmSegMobileReply.ADAPTER.decode(bytes)
        }
}
