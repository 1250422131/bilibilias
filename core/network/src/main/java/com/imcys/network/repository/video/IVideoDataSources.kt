package com.imcys.network.repository.video

import com.imcys.model.NetworkPlayerPlayUrl
import com.imcys.model.PgcPlayUrl
import com.imcys.model.PgcViewSeason
import com.imcys.model.ViewDetail
import com.imcys.model.video.ArchiveCoins
import com.imcys.model.video.ArchiveHasLike
import com.imcys.model.video.VideoFavoured

interface IVideoDataSources {
    suspend fun getView(bvId: String, refresh: Boolean = false): ViewDetail
    suspend fun getView(aid: Long, refresh: Boolean = false): ViewDetail
    suspend fun 获取视频播放地址(aId: Long, bvId: String, cId: Long): NetworkPlayerPlayUrl
    suspend fun hasLike(bvid: String): ArchiveHasLike
    suspend fun hasCoin(bvid: String): ArchiveCoins
    suspend fun hasFavoured(bvid: String): VideoFavoured
    suspend fun 获取剧集基本信息(epId: String): PgcViewSeason
    suspend fun 获取剧集播放地址(epID: Long, aId: Long, cId: Long): PgcPlayUrl
    suspend fun shortLink(url: String): String
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
