package com.bilias.core.domain

import com.imcys.model.space.SeasonsSeriesList
import com.imcys.network.repository.user.IUserDataSources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetVideoInSeries @Inject constructor(private val userRepository: IUserDataSources) {
    operator fun invoke(mId: Long, aId: Long): Flow<SeasonsSeriesList.ItemsLists.SeasonsSeries?> =
        flow {
            emit(userRepository.seasonsSeriesList(mId, 1))
        }
            .flatMapConcat {
                flowOf(it.itemsLists.seriesList + it.itemsLists.seasonsList)
            }
            .transform { seasonsSeries ->
                val series = seasonsSeries.find {
                    aId in it.recentAids
                }
                emit(series)
            }
}
