package com.imcys.network.fake

import com.imcys.model.PgcPlayUrl
import com.imcys.model.PgcViewSeason
import com.imcys.model.NetworkPlayerPlayUrl
import com.imcys.model.ViewDetail
import com.imcys.model.video.ArchiveCoins
import com.imcys.model.video.ArchiveHasLike
import com.imcys.model.video.VideoFavoured
import com.imcys.network.repository.video.IVideoDataSources

class FakeVideoDataSources : IVideoDataSources {
    override suspend fun getView(bvId: String, refresh: Boolean): ViewDetail {
        TODO("Not yet implemented")
    }

    override suspend fun getView(aid: Long, refresh: Boolean): ViewDetail {
        TODO("Not yet implemented")
    }

    override suspend fun 获取视频播放地址(aId: Long, bvId: String, cId: Long): NetworkPlayerPlayUrl {
        TODO("Not yet implemented")
    }

    override suspend fun hasLike(bvid: String): ArchiveHasLike {
        TODO("Not yet implemented")
    }

    override suspend fun hasCoin(bvid: String): ArchiveCoins {
        TODO("Not yet implemented")
    }

    override suspend fun hasFavoured(bvid: String): VideoFavoured {
        TODO("Not yet implemented")
    }

    override suspend fun 获取剧集基本信息(epId: String): PgcViewSeason {
        TODO("Not yet implemented")
    }

    override suspend fun 获取剧集播放地址(epID: Long, aId: Long, cId: Long): PgcPlayUrl {
        TODO("Not yet implemented")
    }

    override suspend fun shortLink(url: String): String {
        TODO("Not yet implemented")
    }
}
