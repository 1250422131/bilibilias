package com.imcys.network.repository.video

import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails

interface IVideoDataSources {
    suspend fun viewDetail(bvid: String): VideoDetails
    suspend fun viewDetail(aid: Long): VideoDetails
    suspend fun getPlayerPlayUrl(bvid: String, cid: Long): PlayerInfo

    companion object {
        const val REQUIRED_DASH = 16
        const val REQUIRED_HDR = 64
        const val REQUIRED_4K = 128
        const val REQUIRED_DOUBLY_AUDIO = 256
        const val REQUIRED_DOUBLY_VIDEO = 512
        const val REQUIRED_8K = 1024
        const val REQUIRED_AV1 = 2048
        const val REQUIRED_ALL = REQUIRED_DASH or
                REQUIRED_HDR or
                REQUIRED_4K or
                REQUIRED_DOUBLY_AUDIO or
                REQUIRED_DOUBLY_VIDEO or
                REQUIRED_8K or
                REQUIRED_AV1
    }
}
