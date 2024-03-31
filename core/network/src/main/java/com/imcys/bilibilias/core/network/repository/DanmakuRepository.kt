package com.imcys.bilibilias.core.network.repository

import com.imcys.bilibilias.core.network.api.BilibiliApi
import com.imcys.bilibilias.core.network.di.WrapperClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.Deflate
import io.ktor.utils.io.core.readBytes
import javax.inject.Inject

class DanmakuRepository @Inject constructor(
    wrapperClient: WrapperClient
) {
    private val client = wrapperClient.client

    suspend fun getRealTimeDanmaku(cid: Long): ByteArray {
        val response = client.get(BilibiliApi.DM_REAL_TIME) {
            parameter("oid", cid)
        }.bodyAsChannel()
        val channel = with(client) {
            with(Deflate) {
                decode(response)
            }
        }
        return channel.readRemaining().readBytes()
    }
}
