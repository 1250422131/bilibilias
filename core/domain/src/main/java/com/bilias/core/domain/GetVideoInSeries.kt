package com.bilias.core.domain

import com.imcys.model.space.SeasonsSeriesList
import com.imcys.network.repository.user.IUserDataSources
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetVideoInSeries @Inject constructor(private val userRepository: IUserDataSources) {
    operator fun invoke(mId: Long, aId: Long) = flow {
        emit(userRepository.seasonsSeriesList(mId, 1))
    }
        .map { it.itemsLists }
        .map {
            it.seriesList
        }.transform {
            for ((index, series) in it.withIndex()) {
                if (aId in series.recentAids) {
                    emit(it[index])
                }
            }
            emit(SeasonsSeriesList.ItemsLists.Series())
        }
}
