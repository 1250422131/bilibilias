package com.bilias.core.domain

import com.imcys.model.video.ToolBarReport
import com.imcys.network.repository.video.IVideoDataSources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetToolbarReportUseCase @Inject constructor(
    private val videoRepository: IVideoDataSources
) {
    operator fun invoke(bvid: String): Flow<ToolBarReport> {
        val likeFlow = flow { emit(videoRepository.hasLike(bvid)) }
        val coinsFlow = flow { emit(videoRepository.hasCoin(bvid)) }
        val favouredFlow = flow { emit(videoRepository.hasFavoured(bvid)) }
        return combine(
            likeFlow,
            coinsFlow,
            favouredFlow
        ) { like, coin, fav ->
            ToolBarReport(
                isLike = like.data == 1,
                isCoin = coin.multiply > 0,
                isFavoured = fav.isFavoured
            )
        }
    }
}
