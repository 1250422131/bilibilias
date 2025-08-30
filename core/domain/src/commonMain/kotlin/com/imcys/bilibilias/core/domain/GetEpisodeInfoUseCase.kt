package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.coroutines.MonoTasker
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.BiliVideoData
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.datastore.model.AudioQuality
import com.imcys.bilibilias.core.domain.model.EpisodeCacheListState
import com.imcys.bilibilias.core.domain.model.EpisodeCacheState
import com.imcys.bilibilias.core.domain.model.EpisodeCacheStatus
import com.imcys.bilibilias.core.domain.model.EpisodeInfo2
import com.imcys.bilibilias.core.domain.model.MediaStream
import com.imcys.bilibilias.core.flow.flowFromSuspend
import com.imcys.bilibilias.core.logging.logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.supervisorScope

class GetEpisodeInfoUseCase(
    private val mediaCacheStorage: MediaCacheDataSource,
    private val api: BilibiliApi,
) {
    private val logger = logger<GetEpisodeInfoUseCase>()
    operator fun invoke(query: String): Flow<EpisodeCacheListState?> {
        return when (val result = TextExtraction.textExtract(query)) {
            is TextExtraction.MatchResult.BV -> bv(result.id)

            TextExtraction.MatchResult.Emptry -> flowOf(null)
        }
    }

    private fun bv(id: String): Flow<EpisodeCacheListState> {
        val detailFlow = flowFromSuspend {
            api.getVideoInfoDetail(id)
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
                supervisorScope {
                    EpisodeCacheState(
                        episodeId = detail.bvid,
                        episodeSubId = cid,
                        index = page.page,
                        title = page.part,
                        cacheStatus = cacheStatus,
                        actionTasker = MonoTasker(this)
                    )
                }
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
        val playUrl = api.getPlayUrl(bvid, cid)
        // 后端发送过来的所有画质选项
        val backendQualityDescriptions =
            playUrl.acceptQuality.zip(playUrl.acceptDescription).toMap()

        // 可以播放的画质选项
        val dash = playUrl.dash
        dash.video.map { it.id }.toSet()
        val videoStreams = dash.video.mapNotNull { video ->
            val qualityDescription = backendQualityDescriptions[video.id]
            if (qualityDescription == null) {
                logger.warn { "Missing description for video quality ID: ${video.id}" }
                null
            } else {
                MediaStream(
                    id = video.id,
                    description = qualityDescription
                )
            }
        }
        val audioList = buildList {
            addAll(dash.audio)
            dash.dolby.audio?.let { addAll(it) }
            dash.flac?.audio?.let { add(it) }
        }
        val audioStreams = audioList.mapNotNull { audioQuality ->
            val quality = AudioQuality.fromCode(audioQuality.id)
            quality?.let {
                MediaStream(
                    id = it.code,
                    description = it.description
                )
            }
        }
        return videoStreams to audioStreams
    }

    private fun BiliVideoData.toEpisodeInfo(): EpisodeInfo2 {
        return EpisodeInfo2(
            title = title,
            desc = desc,
            cover = pic
        )
    }
}