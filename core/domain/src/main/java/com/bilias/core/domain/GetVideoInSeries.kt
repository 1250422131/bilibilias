package com.bilias.core.domain

import com.imcys.model.space.SeasonsArchives
import com.imcys.network.repository.user.IUserDataSources
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetVideoInSeries @Inject constructor(private val userRepository: IUserDataSources) {
    operator fun invoke(mId: Long, aId: Long): Flow<ImmutableList<SeasonsArchives>?> =
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
                val result = series?.let {
                    val meta = it.meta
                    var r3 = persistentListOf<SeasonsArchives>()
                    val r1 = meta.id1?.let { it1 ->
                        userRepository.seasonsArchivesList(
                            meta.mid,
                            it1,
                            meta.total
                        )
                    }
                    val r2 = meta.id2?.let { it1 ->
                        userRepository.seasonsArchivesList(
                            meta.mid,
                            it1,
                            meta.total
                        )
                    }
                    if (r1 != null) {
                        r3 += r1
                    }
                    if (r2 != null) {
                        r3 += r2
                    }
                    r3
                }

                emit(result?.toImmutableList())
            }
}
