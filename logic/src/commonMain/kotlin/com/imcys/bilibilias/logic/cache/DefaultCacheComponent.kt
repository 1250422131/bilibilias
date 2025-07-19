package com.imcys.bilibilias.logic.cache

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.logic.utils.DataStoreProvider
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

interface CacheComponent {
    val stateFlow: StateFlow<List<CacheEpisodeState>>
}

class DefaultCacheComponent(
    componentContext: ComponentContext
) : CacheComponent, ComponentContext by componentContext {
    private val httpDownloader = DataStoreProvider.httpDownloader
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage
    override val stateFlow = MediaCacheManager.cachedEpisodesFlow()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}