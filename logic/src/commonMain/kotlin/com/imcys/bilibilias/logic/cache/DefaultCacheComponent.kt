package com.imcys.bilibilias.logic.cache

import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.logic.utils.createDataStoreMediaCacheStorage
import com.imcys.bilibilias.logic.utils.createKtorPersistentHttpDownloader
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface CacheComponent {
    val uiState: StateFlow<List<EpisodeCacheState>>
}

class DefaultCacheComponent(
    componentContext: ComponentContext
) : CacheComponent, ComponentContext by componentContext {
    private val httpDownloader = createKtorPersistentHttpDownloader()
    private val mediaCacheStorage = createDataStoreMediaCacheStorage()
    override val uiState = mediaCacheStorage.listFlow.map { mediaCacheList ->
        mediaCacheList.map { save ->
            EpisodeCacheState(
                episodeMetadata = save.origin,
            )
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}