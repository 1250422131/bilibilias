package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo
import com.imcys.bilibilias.core.model.EpisodeInfo
import com.imcys.bilibilias.core.model.Owner
import com.imcys.bilibilias.core.model.StreamData

class MediaSourceSelectedUseCase {
    suspend operator fun invoke(qualityLevel: Int, bvid: String, cid: Long): EpisodeInfo {
        val detail = BilibiliApi.getVideoInfoDetail(bvid)
        val playUrl = BilibiliApi.getPlayUrl(bvid, cid)
        return EpisodeInfo(
            bvid = bvid,
            desc = detail.desc,
            cover = detail.pic,
            title = detail.title,
            owner = Owner(detail.owner.mid, detail.owner.face, detail.owner.name),
            parts = emptyList(),
            video = playUrl.dash.video.mediaSelect { list ->
                list.filter {
                    it.id == qualityLevel
                }.maxBy {
                    it.codecid
                }
            },
            audio = playUrl.dash.audio.mediaSelect { list ->
                list.maxByOrNull { Int.MAX_VALUE } ?: list.maxBy { it.id }
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