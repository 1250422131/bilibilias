package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.model.video.Aid
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Cid
import com.imcys.bilibilias.core.model.video.Sources
import com.imcys.bilibilias.core.network.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVideoInfoMetaWithPlayUrlUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    operator fun invoke(bvid: Bvid): Flow<List<VideoInfo>> {
        val detail = flow { emit(videoRepository.获取视频详细信息(bvid)) }
        return detail.map { info ->
            info.pages.map {
                val url = videoRepository.playerPlayUrl(info.aid, info.bvid, it.cid)
                VideoInfo(info.aid, info.bvid, it.cid, info.title, it.part, url.dash.video, url.dash.audio)
            }
        }
    }
}

data class VideoInfo(
    val aid: Aid,
    val bvid: Bvid,
    val cid: Cid,
    val title: String,
    val partTitle: String,
    val video: List<Sources>,
    val audio: List<Sources>,
)
