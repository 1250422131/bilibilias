package com.imcys.bilibilias.logic.cache

import co.touchlab.kermit.Logger
import com.imcys.bilibilias.core.context.KmpContext
import com.imcys.bilibilias.core.domain.GetCachedEpisodeStateUseCase
import com.imcys.bilibilias.core.domain.model.CacheEpisodeState
import com.imcys.bilibilias.core.ffmpeg.createMediaMultiplexer
import com.imcys.bilibilias.core.media.cache.MediaCacheStorage
import com.imcys.bilibilias.core.storage.AsMediaStore
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import kotlin.time.Clock

interface CacheComponent {
    val stateFlow: StateFlow<List<CacheEpisodeState>>

    val canProcess: StateFlow<Boolean>
    fun deleteEpisodeCache(state: CacheEpisodeState)
    fun onCombine(state: CacheEpisodeState)
}

class DefaultCacheComponent(
    componentContext: AppComponentContext,
) : CacheComponent, AppComponentContext by componentContext {
    private val getCachedEpisodeStateUseCase by inject<GetCachedEpisodeStateUseCase>()
    private val mediaCacheStorage by inject<MediaCacheStorage>()
    private val lock = MutableStateFlow(false)
    private val multiplexer = createMediaMultiplexer()
    override val canProcess = multiplexer.isRunning.map { !it }.stateInBackground(true)
    override val stateFlow = getCachedEpisodeStateUseCase()
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun onCombine(state: CacheEpisodeState) {
        if (lock.value) {
            logger.i { "Muxing task for ${state.episodeMetadata} rejected: Another muxing task is in progress." }
            return
        }
        lock.update { true }
        try {
            logger.d { "Attempting to combine media cache for episode: ${state.episodeMetadata}" }
            val uris = state.mediaCacheMetadata.metadata.map { it.filePath.toString() }
            val filename = Clock.System.now().toEpochMilliseconds()
            val videoUri =
                AsMediaStore.createVideo(KmpContext, filename.toString(), "video/mp4", "BilibiliAs")
                    ?: run {
                        logger.w { "Failed to create video file for episode: ${state.episodeMetadata}" }
                        lock.update { false }
                        return
                    }
            applicationScope.launch {
                multiplexer.muxMedia(uris, videoUri.toString())
            }.invokeOnCompletion {
                lock.update { false }
                logger.d(it) { "Muxing task finished (or failed) for ${state.episodeMetadata}. Lock released." }
            }
        } catch (e: Exception) {
            lock.update { false }
            logger.e(e) { "An unexpected error occurred before starting muxing for ${state.episodeMetadata}" }
        }
    }

    override fun deleteEpisodeCache(state: CacheEpisodeState) {
        applicationScope.launch {
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