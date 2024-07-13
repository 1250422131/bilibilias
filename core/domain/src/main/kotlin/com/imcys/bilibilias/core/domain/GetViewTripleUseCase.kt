package com.imcys.bilibilias.core.domain

import com.imcys.bilibilias.core.model.video.ViewTriple
import com.imcys.bilibilias.core.network.repository.VideoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetViewTripleUseCase @Inject constructor(private val videoRepository: VideoRepository) {
    operator fun invoke(bvid: String): Flow<ViewTriple> {
        val likeFlow = flow { emit(videoRepository.getArchiveLike(bvid)) }
        val coinsFlow = flow { emit(videoRepository.getArchiveCoins(bvid)) }
        val favouredFlow = flow { emit(videoRepository.getArchiveFavoured(bvid)) }

        return combine(likeFlow, coinsFlow, favouredFlow) { like, coins, favoured ->
            ViewTriple(like, coins, favoured)
        }
    }
}
