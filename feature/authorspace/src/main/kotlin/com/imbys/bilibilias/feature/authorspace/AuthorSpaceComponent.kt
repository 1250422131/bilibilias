package com.imbys.bilibilias.feature.authorspace

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.video.Mid
import com.imcys.bilibilias.core.ui.UnitedDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthorSpaceComponent {
    val models: StateFlow<Model>
    val flow: Flow<PagingData<UnitedDetails>>

    data class Model(val units: List<String>)
    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            mid: Mid,
        ): AuthorSpaceComponent
    }
}
