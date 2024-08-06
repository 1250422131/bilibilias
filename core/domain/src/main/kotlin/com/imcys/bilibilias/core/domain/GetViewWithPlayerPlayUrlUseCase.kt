package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetViewWithPlayerPlayUrlUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    operator fun invoke(bvid: String): Flow<Pair<ViewDetail, VideoStreamUrl>> {
        val detailFlow = flow { emit(videoRepository.获取视频详细信息(bvid)) }
        val playerPlayUrlFlow = detailFlow.map { detail ->
            videoRepository.playerPlayUrl(detail.aid, detail.bvid, detail.cid)
        }
        return combine(detailFlow, playerPlayUrlFlow, ::Pair)
    }
}
