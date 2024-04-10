package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.model.bangumi.BangumiDetail
import com.imcys.bilibilias.core.model.bangumi.BangumiStreamUrl
import com.imcys.bilibilias.core.network.repository.BangumiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetStreamWithBangumiDetailUseCase @Inject constructor(
    private val bangumiRepository: BangumiRepository
) {
    operator fun invoke(epid: Long): Flow<Pair<BangumiDetail, List<BangumiWithStream>>> {
        val detailFlow = flow { emit(bangumiRepository.获取剧集详情(epid)) }
        val collectionStreamUrl = detailFlow.map { detail ->
            detail.episodes.map {
                val streamUrl = bangumiRepository.videoStreamingURL(it.aid, it.cid, it.epId)
                BangumiWithStream(it.title, it.aid, it.bvid, it.cid, it.epId, streamUrl)
            }
        }
        val streamUrl = detailFlow.flatMapLatest {
            collectionStreamUrl
        }
        return combine(detailFlow, streamUrl, ::Pair)
    }
}

data class BangumiWithStream(
    val title: String,
    val aid: Long,
    val bvid: String,
    val cid: Long,
    val epid: Long,
    val streamUrl: BangumiStreamUrl
)
