package com.imcys.bilibilias.feature.home

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.model.bilibilias.HomeBanner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {
    val models: StateFlow<Model>
    fun take(event: HomeEvent)
    data class Model(
        val updateNotice: UpdateNotice,
        val homeBanner: HomeBanner,
    )

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
        ): HomeComponent
    }
}
