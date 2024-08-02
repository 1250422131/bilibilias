package com.imbys.bilibilias.feature.authorspace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.network.pagingsource.SpaceArcSearchPagingSource
import com.imcys.bilibilias.core.network.repository.UserSpaceRepository
import com.imcys.bilibilias.core.ui.UnitedDetails
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultAuthorSpaceComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val mid: Mid,
    private val spaceArcSearchPagingSourceFactory: SpaceArcSearchPagingSource.Factory,
    private val userSpaceRepository: UserSpaceRepository,
) : AuthorSpaceComponent, BaseViewModel<Unit, AuthorSpaceComponent.Model>(componentContext) {
    override val flow = Pager(
        PagingConfig(
            pageSize = 1,
            initialLoadSize = 1,
        ),
        1,
    ) {
        spaceArcSearchPagingSourceFactory(mid)
    }.flow
        .map { pagingData ->
            pagingData.map {
                UnitedDetails(it.pic, it.title)
            }
        }
        .cachedIn(viewModelScope)

    @Composable
    override fun models(events: Flow<Unit>): AuthorSpaceComponent.Model {
        LaunchedEffect(Unit) {
            val arc = userSpaceRepository.查询用户投稿视频(mid, 1)
            Napier.d { arc.toString() }
        }

        return AuthorSpaceComponent.Model(emptyList())
    }

    @AssistedFactory
    interface Factory : AuthorSpaceComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): DefaultAuthorSpaceComponent
    }
}
