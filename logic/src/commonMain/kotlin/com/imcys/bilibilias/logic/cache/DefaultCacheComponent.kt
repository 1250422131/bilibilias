package com.imcys.bilibilias.logic.cache

import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.data.DataStoreProvider
import com.imcys.bilibilias.core.data.MediaCacheManager
import com.imcys.bilibilias.core.data.model.CacheEpisodeState
import com.imcys.bilibilias.core.ffmpeg.createMediaMultiplexer
import com.imcys.bilibilias.core.storage.AsMediaStore
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock

interface CacheComponent {
    val stateFlow: StateFlow<List<CacheEpisodeState>>

    fun deleteEpisodeCache(state: CacheEpisodeState)
    fun onCombine(state: CacheEpisodeState)
}

class DefaultCacheComponent(
    componentContext: AppComponentContext
) : CacheComponent, AppComponentContext by componentContext {
    private val multiplexer = createMediaMultiplexer()
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage
    override val stateFlow = MediaCacheManager.observeCachedEpisodeStates()
        .stateIn(
            scope = backgroundScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun onCombine(state: CacheEpisodeState) {
        logger.d { "Attempting to combine media cache for episode: ${state.episodeMetadata}" }
        val uris = state.mediaCacheMetadata.metadata.map { it.filePath.toString() }
        val filename = Clock.System.now().toEpochMilliseconds()
        val videoUri =
            AsMediaStore.createVideo(KmpContext, filename.toString(), "video/mp4", "BilibiliAs")
                ?: return
        val uris1 = uris + videoUri.toString()
        logger.d { uris1.joinToString() }
        backgroundScope.launch {
            multiplexer.muxMedia(uris, videoUri.toString())
        }
    }

    override fun deleteEpisodeCache(state: CacheEpisodeState) {
        backgroundScope.launch {
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