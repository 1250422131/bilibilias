package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.model.video.VideoStreamUrl
import com.imcys.bilibilias.core.model.video.ViewDetail
import com.imcys.bilibilias.core.network.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStreamWithVideoDetailUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
) {
    operator fun invoke(bvid: String): Flow<Pair<ViewDetail, List<CollectionDetailWithStream>>> {
        val detailFlow = flow { emit(videoRepository.获取视频详细信息(bvid)) }
        val collectionStreamUrl = detailFlow.map { detail ->
            detail.pages.map {
                val streamUrl = videoRepository.playerPlayUrl(detail.aid, detail.bvid, it.cid)
                CollectionDetailWithStream(it.part, it.cid, streamUrl)
            }
        }
        val streamUrl = detailFlow.flatMapLatest {
            collectionStreamUrl
        }
        return combine(detailFlow, streamUrl, ::Pair)
    }
}

data class CollectionDetailWithStream(
    val title: String,
    val cid: Long,
    val streamUrl: VideoStreamUrl,
)
