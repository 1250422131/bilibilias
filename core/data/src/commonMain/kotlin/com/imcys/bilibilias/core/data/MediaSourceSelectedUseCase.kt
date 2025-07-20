package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.model.EpisodeInfo
import com.imcys.bilibilias.core.model.Owner
import com.imcys.bilibilias.core.model.StreamData

class MediaSourceSelectedUseCase {
    suspend operator fun invoke(
        episodeId: String,
        request: EpisodeCacheRequest
    ): EpisodeInfo {
        val detail = BilibiliApi.getVideoInfoDetail(episodeId)
        val playUrl = BilibiliApi.getPlayUrl(detail.bvid, detail.cid)
        return EpisodeInfo(
            bvid = detail.bvid,
            cid = detail.cid,
            desc = detail.desc,
            cover = detail.pic,
            title = detail.title,
            owner = Owner(detail.owner.mid, detail.owner.face, detail.owner.name),
            parts = emptyList(),
            video = playUrl.dash.video.mediaSelect { list ->
                list.filter {
                    it.id == request.videoResolution
                }.maxBy {
                    it.codecid
                }
            },
            audio = playUrl.dash.audio.mediaSelect { list ->
                list.maxByOrNull { request.audioResolution } ?: list.maxBy { it.id }
            }
        )
    }

    private fun VideoPlaybackInfo.AudioOrVideo.asStreamData(): StreamData {
        return StreamData(
            id = id,
            baseUrl = baseUrl,
            backupUrl = backupUrl,
        )
    }

    private fun List<VideoPlaybackInfo.AudioOrVideo>.mediaSelect(
        condition: (List<VideoPlaybackInfo.AudioOrVideo>) -> VideoPlaybackInfo.AudioOrVideo
    ): List<StreamData> {
        return listOf(condition(this).asStreamData())
    }
}