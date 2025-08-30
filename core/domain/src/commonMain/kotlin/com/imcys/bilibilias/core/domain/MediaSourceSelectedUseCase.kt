package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.datasource.Cdn
import com.imcys.bilibilias.core.datasource.CdnType
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeInfo
import com.imcys.bilibilias.core.domain.model.MediaStreamMetadata
import com.imcys.bilibilias.core.domain.model.Resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

typealias MediaStreamTransformCondition = (Map<Int, List<MediaStreamMetadata>>) -> MediaStreamMetadata

class MediaSourceSelectedUseCase(
    private val api: BilibiliApi,
) {
    suspend operator fun invoke(
        request: EpisodeCacheRequest
    ): EpisodeInfo {
        return withContext(Dispatchers.IO) {
            val detailDeferred = async { api.getVideoInfoDetail(request.episodeId) }
            val playUrlDeferred =
                async { api.getPlayUrl(request.episodeId, request.episodeSubId) }

            val detail = detailDeferred.await()
            val playUrl = playUrlDeferred.await()
            val audioUrl = playUrl.dash.audio.applyMediaStreamTransformation { streamMap ->
                streamMap.values.flatten().maxBy { it.id }
            }
            val videoUrl = playUrl.dash.video.applyMediaStreamTransformation { streamMap ->
                val preferredResolutionMetadata = streamMap[request.videoResolution]
                preferredResolutionMetadata?.maxBy { it.codecId }
                    ?: streamMap.values.flatten().maxWith(compareBy { it.id })
            }
            EpisodeInfo(
                bvid = detail.bvid,
                cid = detail.cid,
                title = detail.title,
                urls = listOf(audioUrl, videoUrl)
            )
        }
    }

    private fun List<VideoPlaybackInfo.AudioOrVideo>.applyMediaStreamTransformation(
        action: MediaStreamTransformCondition
    ): MediaStreamMetadata {
        val mediaStreamMetadataMap = this.map { playbackInfo ->
            val allBackupUrls = playbackInfo.primaryBackupUrls + playbackInfo.secondaryBackupUrls
            val cdnResources = allBackupUrls.mapNotNull { url -> Cdn.parse(url) }
                .filterNot { it.type == CdnType.MCDN }
            MediaStreamMetadata(
                playbackInfo.id,
                cdnResources,
                playbackInfo.codecid,
                Resolution.values().find { it.id == playbackInfo.id }?.displayName ?: ""
            )
        }.groupBy { it.id }
        return action(mediaStreamMetadataMap)
    }
}