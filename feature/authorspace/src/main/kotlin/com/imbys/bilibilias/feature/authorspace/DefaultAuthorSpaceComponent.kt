package com.imbys.bilibilias.feature.authorspace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.network.repository.UserSpaceRepository
import com.imcys.bilibilias.feature.common.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class DefaultAuthorSpaceComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val mid: Mid,
    private val userSpaceRepository: UserSpaceRepository,
) : AuthorSpaceComponent, BaseViewModel<Unit, AuthorSpaceComponent.Model>(componentContext) {
    @Composable
    override fun models(events: Flow<Unit>) : AuthorSpaceComponent.Model {
        LaunchedEffect(Unit) {
            userSpaceRepository.查询用户投稿视频(mid)
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
