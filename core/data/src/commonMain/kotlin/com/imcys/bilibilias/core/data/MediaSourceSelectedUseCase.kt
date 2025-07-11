package com.imcys.bilibilias.core.data

import com.imcys.bilibilias.core.data.model.CacheData
import com.imcys.bilibilias.core.data.model.MediaSources
import com.imcys.bilibilias.core.datasource.api.BilibiliApi
import com.imcys.bilibilias.core.datasource.model.VideoPlaybackInfo

class MediaSourceSelectedUseCase {
    suspend operator fun invoke(qualityLevel: Int, bvid: String, cid: Long): CacheData {
        val detail = BilibiliApi.getVideoInfoDetail(bvid)
        val playUrl = BilibiliApi.getPlayUrl(bvid, cid)
        val video = mediaSelect(playUrl.dash.video, qualityLevel)
        val audio = mediaSelect(playUrl.dash.audio)
        return CacheData(
            bvid = detail.bvid,
            cid = detail.cid,
            title = detail.title,
            cover = detail.pic,
            pub = detail.pubDate,
            videos = MediaSources(
                video.baseUrl,
                video.id,
                video.codecid,
                video.height,
                video.width
            ),
            audios = MediaSources(audio.baseUrl, audio.id)
        )
    }

    private fun mediaSelect(
        videos: List<VideoPlaybackInfo.AudioOrVideo>,
        qualityLevel: Int
    ): VideoPlaybackInfo.AudioOrVideo {
        return videos.filter { it.id == qualityLevel }.maxBy { it.codecid }
    }

    private fun mediaSelect(audios: List<VideoPlaybackInfo.AudioOrVideo>): VideoPlaybackInfo.AudioOrVideo {
        return audios.maxBy { it.id }
    }
}