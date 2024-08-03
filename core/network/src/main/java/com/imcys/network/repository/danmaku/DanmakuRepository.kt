package com.imcys.network.repository.danmaku

import com.imcys.bilibilias.dm.*
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

    /**
     * type=1&oid=1395668165&pid=965787633&segment_index=1&pull_mode=1&ps=0&pe=120000&web_location=1315873&w_rid=c53b777459eee0b177c70f8379976047&wts=1704981456
     * type=1&oid=1395668165&pid=965787633&segment_index=2                           &web_location=1315873&w_rid=8defe0a3e60b468f55c0911e756e9207&wts=1704981491
     */
    override suspend fun protoWbi(aId: Long, cid: Long, index: Int, type: Int): DmSegMobileReply {
        val bytes = client.get("x/v2/dm/web/seg.so") {
            parameter("type", type)
            parameter("oid", cid)
            parameter("pid", aId)
            parameter("segment_index", index)
        }.bodyAsChannel()
            .readRemaining()
            .readBytes()
        return DmSegMobileReply.ADAPTER.decode(bytes)
    }
}
