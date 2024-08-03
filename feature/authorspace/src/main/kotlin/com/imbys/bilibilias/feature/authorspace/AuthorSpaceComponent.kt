package com.imbys.bilibilias.feature.authorspace

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Bvid
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.ui.UnitedDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthorSpaceComponent {
    val flow: Flow<PagingData<UnitedDetails>>
    val models: StateFlow<Model>

    data class Model(val units: List<UnitedDetails>)

    fun take(event: AuthorSpaceEvent)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): AuthorSpaceComponent
    }
}
