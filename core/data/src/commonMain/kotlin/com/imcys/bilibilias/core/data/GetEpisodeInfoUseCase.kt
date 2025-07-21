package com.imcys.bilibilias.core.data

import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.data.TextExtraction.textExtract
import com.imcys.bilibilias.core.data.model.EpisodeCacheListState
import com.imcys.bilibilias.core.data.model.EpisodeCacheState
import com.imcys.bilibilias.core.data.model.EpisodeCacheStatus
import com.imcys.bilibilias.core.data.model.EpisodeInfo2
import com.imcys.bilibilias.core.data.model.MediaStream
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.flow.flowFromSuspend
import com.imcys.bilibilias.core.media.cache.MediaCacheStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class GetEpisodeInfoUseCase(
    private val mediaCacheStorage: MediaCacheStorage
) {
    operator fun invoke(query: String): Flow<EpisodeCacheListState?> {
        return when (val result = textExtract(query)) {
            is TextExtraction.MatchResult.BV -> bv(result.id)

            TextExtraction.MatchResult.Error -> flowOf(null)
        }
    }

    private fun bv(id: String): Flow<EpisodeCacheListState> {
        val detailFlow = flowFromSuspend {
            BilibiliApi.getVideoInfoDetail(id)
        }

        return detailFlow.combine(mediaCacheStorage.listFlow) { detail, cachedItemsList ->
            val (videoStream, audioStream) = fetchMediaStreams(detail.bvid, detail.cid)

            val cachedItemsByCid = mediaCacheStorage.listFlow.first()
                .filter { it.origin.bvid == detail.bvid }
                .associateBy { it.origin.cid }

            val states = detail.pages.map { page ->
                val cid = page.cid
                val cacheStatus = if (cachedItemsByCid.containsKey(cid)) {
                    EpisodeCacheStatus.Cached
                } else {
                    EpisodeCacheStatus.NotCached
                }
                EpisodeCacheState(cid, page.part, cacheStatus)
            }
            EpisodeCacheListState(
                episodeInfo = detail.toEpisodeInfo(),
                episodes = states,
                videoStreams = videoStream,
                audioStreams = audioStream
            )
        }
    }

    private suspend fun fetchMediaStreams(
        bvid: String,
        cid: Long
    ): Pair<List<MediaStream>, List<MediaStream>> {
        val playUrl = BilibiliApi.getPlayUrl(bvid, cid)
        // 后端发送过来的所有画质选项
        val backendQualityDescriptions =
            playUrl.acceptQuality.zip(playUrl.acceptDescription).toMap()

        // 可以播放的画质选项
        val playerControlQuality = playUrl.dash.video.map { it.id }.toSet()
        val videoStreams = playUrl.dash.video.mapNotNull { video ->
            val qualityDescription = backendQualityDescriptions[video.id]
            if (qualityDescription == null) {
                Logger.w { "Missing description for video quality ID: ${video.id}" }
                null
            } else {
                MediaStream(
                    id = video.id,
                    description = qualityDescription
                )
            }
        }
        val audioStreams = playUrl.dash.audio.map { audioQuality ->
            MediaStream(
                id = audioQuality.id,
                description = ""
            )
        }
        return videoStreams to audioStreams
    }

    private fun BiliVideoData.toEpisodeInfo(): EpisodeInfo2 {
        return EpisodeInfo2(
            episodeId = bvid,
            episodeSubId = cid,
            title = title,
            desc = desc,
            cover = pic
        )
    }
}