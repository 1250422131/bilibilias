package com.imcys.bilibilias.logic.cache

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.imcys.bilibilias.core.data.DataStoreProvider
import com.imcys.bilibilias.core.data.MediaCacheManager
import com.imcys.bilibilias.core.data.model.CacheEpisodeState
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

interface CacheComponent {
    val stateFlow: StateFlow<List<CacheEpisodeState>>

    fun deleteEpisodeCache(state: CacheEpisodeState)
}

class DefaultCacheComponent(
    componentContext: ComponentContext
) : CacheComponent, ComponentContext by componentContext {
    private val httpDownloader = DataStoreProvider.httpDownloader
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage
    override val stateFlow = MediaCacheManager.observeCachedEpisodeStates()
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun deleteEpisodeCache(state: CacheEpisodeState) {
        scope.launch {
            try {
                // todo 下载记录也要删除
                Logger.d { "Attempting to delete media cache metadata for episode: ${state.episodeMetadata}" }
                val metadataSuccess = state.mediaCacheMetadata.delete()
                if (metadataSuccess) {
                    Logger.d { "Media cache metadata deleted successfully." }

                    Logger.d { "Attempting to delete media cache storage for episode: ${state.episodeMetadata}" }
                    val storageDeleted = mediaCacheStorage.delete(state.episodeMetadata)
                    if (!storageDeleted) {
                        Logger.w { "Failed to delete from media cache storage, but metadata might be deleted." }
                        return@launch
                    }
                    Logger.d { "Media cache storage deleted successfully." }
                } else {
                    Logger.w { "Failed to delete media cache metadata." }
                }
            } catch (e: Exception) {
                Logger.e(e) { "Error deleting cache for episode: ${state.episodeMetadata}" }
            }
        }
    }
}