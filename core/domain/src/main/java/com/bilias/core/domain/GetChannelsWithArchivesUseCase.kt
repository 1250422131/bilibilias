package com.bilias.core.domain

import com.imcys.model.space.ChannelsWithArchives
import com.imcys.network.repository.user.IUserDataSources
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetChannelsWithArchivesUseCase @Inject constructor(private val userRepository: IUserDataSources) {
    operator fun invoke(mId: Long, aId: Long): Flow<ChannelsWithArchives.ItemsLists.ChannelItem?> =
        flow {
            emit(userRepository.getChannelsWithArchives(mId, 1))
        }
            .flatMapConcat {
                Napier.d { "alll ${it.items}" }
                flowOf(it.items.seasons + it.items.series)
            }
            .transform {
                for (c in it) {
                    if (aId in c.recentAids) {
                        return@transform emit(c)
                    }
                }
                emit(null)
            }
}
