package com.imcys.network.repository.danmaku

import com.imcys.bilibilias.dm.DmSegMobileReply
import com.imcys.network.wbiGet
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.core.readBytes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DanmakuRepository @Inject constructor(
    private val client: HttpClient,
) : IDanmakuDataSources {
    override suspend fun xml(cid: Long): ByteArray =
        client.get("x/v1/dm/list.so") {
            parameter("oid", cid)
        }
            .bodyAsChannel()
            .readRemaining()
            .readBytes()

    override suspend fun protoWbi(cid: Long, index: Int, type: Int): DmSegMobileReply {
        val bytes = client.wbiGet("x/v2/dm/wbi/web/seg.so") {
            parameter("oid", cid)
            parameter("type", type)
            parameter("segment_index", index)
        }.bodyAsChannel()
            .readRemaining()
            .readBytes()
        return DmSegMobileReply.ADAPTER.decode(bytes)
    }
}
