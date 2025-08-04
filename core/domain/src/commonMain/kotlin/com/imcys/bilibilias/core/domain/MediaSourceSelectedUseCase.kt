package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.datasource.Cdn
import com.imcys.bilibilias.core.datasource.CdnType
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeInfo
import com.imcys.bilibilias.core.domain.model.MediaStreamMetadata
import com.imcys.bilibilias.core.domain.model.Owner
import com.imcys.bilibilias.core.domain.model.Resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

typealias MediaStreamTransformCondition = (Map<Int, List<MediaStreamMetadata>>) -> MediaStreamMetadata

class MediaSourceSelectedUseCase {
    suspend operator fun invoke(
        request: EpisodeCacheRequest
    ): EpisodeInfo {
        return withContext(Dispatchers.IO) {
            val detailDeferred = async { BilibiliApi.getVideoInfoDetail(request.episodeId) }
            val playUrlDeferred =
                async { BilibiliApi.getPlayUrl(request.episodeId, request.episodeSubId) }

            val detail = detailDeferred.await()
            val playUrl = playUrlDeferred.await()

            EpisodeInfo(
                bvid = detail.bvid,
                cid = detail.cid,
                desc = detail.desc,
                cover = detail.pic,
                title = detail.title,
                owner = Owner(
                    detail.owner.mid,
                    detail.owner.face,
                    detail.owner.name
                ),
                video = playUrl.dash.video.applyMediaStreamTransformation { streamMap ->
                    val preferredResolutionMetadata = streamMap[request.videoResolution]
                    preferredResolutionMetadata?.maxBy { it.codecId }
                        ?: streamMap.values.flatten().maxWith(compareBy { it.id })
                },
                audio = playUrl.dash.audio.applyMediaStreamTransformation { streamMap ->
                    streamMap.values.flatten().maxBy { it.id }
                }
            )
        }
    }

    private fun List<VideoPlaybackInfo.AudioOrVideo>.applyMediaStreamTransformation(
        action: MediaStreamTransformCondition
    ): MediaStreamMetadata {
        val mediaStreamMetadataMap = this.map { playbackInfo ->
            val allBackupUrls = playbackInfo.backupUrl1 + playbackInfo.backupUrl2
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