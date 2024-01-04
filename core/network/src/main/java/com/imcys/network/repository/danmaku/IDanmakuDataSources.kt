package com.imcys.network.repository.danmaku

import com.imcys.bilibilias.dm.DmSegMobileReply

interface IDanmakuDataSources {
    suspend fun xml(cid: Long): ByteArray
    suspend fun protoWbi(cid: Long, index: Int, type: Int = 1): DmSegMobileReply
}
