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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlin.time.Clock

interface CacheComponent {
    val stateFlow: StateFlow<List<CacheEpisodeState>>

    val canProcess: StateFlow<Boolean>
    fun deleteEpisodeCache(state: CacheEpisodeState)
    fun onCombine(state: CacheEpisodeState)
}

class DefaultCacheComponent(
    componentContext: AppComponentContext
) : CacheComponent, AppComponentContext by componentContext {
    private val muxMutex = Mutex()
    private val multiplexer = createMediaMultiplexer()
    override val canProcess = multiplexer.isRunning.map { !it }.stateInBackground(true)
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage
    override val stateFlow = MediaCacheManager.cachedEpisodeStates
        .stateIn(
            scope = backgroundScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun onCombine(state: CacheEpisodeState) {
        if (muxMutex.tryLock()) {
            logger.i { "Muxing task for ${state.episodeMetadata} rejected: Another muxing task is in progress." }
            return
        }

        try {
            logger.d { "Attempting to combine media cache for episode: ${state.episodeMetadata}" }
            val uris = state.mediaCacheMetadata.metadata.map { it.filePath.toString() }
            val filename = Clock.System.now().toEpochMilliseconds()
            val videoUri =
                AsMediaStore.createVideo(KmpContext, filename.toString(), "video/mp4", "BilibiliAs")
                    ?: run { // Use run for cleaner early return
                        logger.w { "Failed to create video file for episode: ${state.episodeMetadata}" }
                        muxMutex.unlock()
                        return
                    }
            backgroundScope.launch {
                multiplexer.muxMedia(uris, videoUri.toString())
            }.invokeOnCompletion {
                muxMutex.unlock() // Pass no owner or the same dedicated owner
                logger.d(it) { "Muxing task finished (or failed) for ${state.episodeMetadata}. Mutex released." }
            }
        } catch (e: Exception) { // Catch potential exceptions outside the coroutine too
            logger.e(e) { "An unexpected error occurred before starting muxing for ${state.episodeMetadata}" }
        } finally {
            muxMutex.unlock()
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