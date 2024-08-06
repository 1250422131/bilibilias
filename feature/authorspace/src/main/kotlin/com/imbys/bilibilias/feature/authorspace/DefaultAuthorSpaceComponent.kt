package com.imbys.bilibilias.feature.authorspace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.utils.addOrRemove
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.network.pagingsource.SpaceArcSearchPagingSource
import com.imcys.bilibilias.core.network.repository.UserSpaceRepository
import com.imcys.bilibilias.core.ui.UnitedDetails
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultAuthorSpaceComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val mid: Mid,
    private val spaceArcSearchPagingSourceFactory: SpaceArcSearchPagingSource.Factory,
    private val downloadManager: DownloadManager,
) : AuthorSpaceComponent,
    BaseViewModel<AuthorSpaceEvent, AuthorSpaceComponent.Model>(componentContext) {
    override val flow = Pager(
        PagingConfig(
            pageSize = 30,
            initialLoadSize = 1,
        ),
        1,
    ) {
        spaceArcSearchPagingSourceFactory(mid)
    }.flow
        .map { pagingData ->
            pagingData.map {
                UnitedDetails(it.pic, it.title, it.bvid)
            }
        }
        .cachedIn(viewModelScope)

    @Composable
    override fun models(events: Flow<AuthorSpaceEvent>): AuthorSpaceComponent.Model {
        var unitedDetails = remember { mutableStateListOf<UnitedDetails>() }
        var selectedIds = remember { mutableStateListOf<Bvid>() }
        LaunchedEffect(Unit) {
            events.collect { event ->
                when (event) {
                    AuthorSpaceEvent.DownloadAll -> downloadManager.download(selectedIds)
                    is AuthorSpaceEvent.ChangeSelection -> selectedIds.addOrRemove(event.id)
                }
            }
        }
        return AuthorSpaceComponent.Model(unitedDetails)
    }

    @AssistedFactory
    interface Factory : AuthorSpaceComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): DefaultAuthorSpaceComponent
    }
}
