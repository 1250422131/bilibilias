package com.imcys.network.fake

import com.imcys.model.PgcPlayUrl
import com.imcys.model.PgcViewSeason
import com.imcys.model.PlayerInfo
import com.imcys.model.VideoDetails
import com.imcys.model.video.ArchiveCoins
import com.imcys.model.video.ArchiveHasLike
import com.imcys.model.video.VideoFavoured
import com.imcys.network.repository.video.IVideoDataSources

class FakeVideoDataSources : IVideoDataSources {
    override suspend fun getDetail(bvid: String): VideoDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getDetail(aid: Long): VideoDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getPlayerPlayUrl(bvid: String, cid: Long): PlayerInfo {
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

    override suspend fun getPgcViewSeason(epId: String): PgcViewSeason {
        TODO("Not yet implemented")
    }

    override suspend fun getPgcPlayUrl(epID: Long, aId: Long, cId: Long): PgcPlayUrl {
        TODO("Not yet implemented")
    }

    override suspend fun shortLink(url: String): String {
        TODO("Not yet implemented")
    }
}
