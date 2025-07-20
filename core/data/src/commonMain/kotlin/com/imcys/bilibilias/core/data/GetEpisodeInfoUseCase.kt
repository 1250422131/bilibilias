package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.TextExtraction.textExtract
import com.imcys.bilibilias.core.data.model.EpisodeCacheListState
import com.imcys.bilibilias.core.data.model.EpisodeCacheState
import com.imcys.bilibilias.core.data.model.EpisodeCacheStatus
import com.imcys.bilibilias.core.data.model.EpisodeInfo2
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.model.EpisodeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class GetEpisodeInfoUseCase {
    operator fun invoke(query: String): Flow<EpisodeInfo?> {
//        return when (val result = textExtract(query)) {
//            is TextExtraction.MatchResult.BV -> {
//                flow {
//                    val detail = BilibiliApi.getVideoInfoDetail(result.id)
//                    val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)
//                    // 后端发送过来的所有画质选项
//                    val backendControlQuality =
//                        playUrl.acceptQuality.zip(playUrl.acceptDescription).toMap()
//                    // 可以播放的画质选项
//                    val playerControlQuality = playUrl.dash.video.map { it.id }.toSet()
//                    val episodeInfo = EpisodeInfo(
//                        bvid = detail.bvid,
//                        cid = detail.cid,
//                        desc = detail.desc,
//                        cover = detail.pic,
//                        title = detail.title,
//                        owner = Owner(detail.owner.mid, detail.owner.face, detail.owner.name),
//                        parts = detail.pages.map { EpisodePartInfo(it.cid, it.part, it.page) },
//                        video = playUrl.dash.video.map {
//                            val q = backendControlQuality[it.id] ?: error("map error")
//                            StreamData(
//                                id = it.id,
//                                baseUrl = it.baseUrl,
//                                backupUrl = it.backupUrl,
//                                codecId = 0,
//                                description = q
//                            )
//                        },
//                        audio = playUrl.dash.audio.map {
//                            StreamData(
//                                id = it.id,
//                                baseUrl = it.baseUrl,
//                                backupUrl = it.backupUrl,
//                            )
//                        },
//                    )
//
//                    emit(episodeInfo)
//                }
//            }
//
//            TextExtraction.MatchResult.Error -> flowOf(null)
//        }
        TODO()
    }

    fun create(query: String): Flow<EpisodeCacheListState?> {
        return when (val result = textExtract(query)) {
            is TextExtraction.MatchResult.BV -> bv(result.id)

            TextExtraction.MatchResult.Error -> flowOf(null)
        }
    }

    private fun bv(id: String): Flow<EpisodeCacheListState> {
        return flow {
            val detail = BilibiliApi.getVideoInfoDetail(id)
//            val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)

            val cachedItemsByCid = DataStoreProvider.mediaCacheStorage.listFlow.first()
                .filter { it.origin.bvid == detail.bvid }
                .associateBy { it.origin.cid } // Create a map for efficient lookup

            val states = detail.pages.map { page ->
                val cid = page.cid
                val cacheStatus = if (cachedItemsByCid.containsKey(cid)) {
                    EpisodeCacheStatus.Cached
                } else {
                    EpisodeCacheStatus.NotCached
                }
                EpisodeCacheState(cid, cacheStatus)
            }
            val listState = EpisodeCacheListState(
                episodeInfo = detail.toEpisodeInfo(),
                episodes = states
            )
            emit(listState)
        }
    }

    fun BiliVideoData.toEpisodeInfo(): EpisodeInfo2 { // Assuming EpisodeInfo is the final name
        return EpisodeInfo2(
            episodeId = bvid,
            episodeSubId = cid,
            title = title,
            desc = desc,
            cover = pic
        )
    }
}