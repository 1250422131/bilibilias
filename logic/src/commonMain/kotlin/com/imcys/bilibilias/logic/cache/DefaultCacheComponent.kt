package com.imcys.bilibilias.logic.cache

import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.domain.GetCachedEpisodeStateUseCase
import com.imcys.bilibilias.core.domain.model.CacheEpisodeState
import com.imcys.bilibilias.core.ffmpeg.MediaMultiplexer
import com.imcys.bilibilias.core.logging.logger
import com.imcys.bilibilias.core.storage.MediaStoreAccess
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
    private val multiplexer: MediaMultiplexer,
    private val mediaStoreAccess: MediaStoreAccess,
) : CacheComponent, AppComponentContext by componentContext {
    private val getCachedEpisodeStateUseCase by inject<GetCachedEpisodeStateUseCase>()
    private val mediaCacheStorage by inject<MediaCacheDataSource>()
    private val lock = MutableStateFlow(false)

    override val canProcess = multiplexer.isRunning.map { !it }.stateInBackground(true)
    override val stateFlow = getCachedEpisodeStateUseCase()
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    private val logger = logger<CacheComponent>()
    override fun onCombine(state: CacheEpisodeState) {
        if (lock.value) {
            logger.info { "Muxing task for ${state.episodeMetadata} rejected: Another muxing task is in progress." }
            return
        }
        lock.update { true }
        try {
            logger.info { "Attempting to combine media cache for episode: ${state.episodeMetadata}" }
            val uris = state.mediaCacheMetadata.metadata.map { it.filePath.toString() }
            val filename = Clock.System.now().toEpochMilliseconds()
            val videoUri =
                mediaStoreAccess.createVideo(filename.toString(), "video/mp4", "BilibiliAs")
                    ?: run {
                        logger.warn { "Failed to create video file for episode: ${state.episodeMetadata}" }
                        lock.update { false }
                        return
                    }
            applicationScope.launch {
                multiplexer.muxMedia(uris, videoUri.toString())
            }.invokeOnCompletion {
                lock.update { false }
                it?.let {
                    logger.debug(it) { "Muxing task finished (or failed) for ${state.episodeMetadata}. Lock released." }
                }
            }
        } catch (e: Exception) {
            lock.update { false }
            logger.error(e) { "An unexpected error occurred before starting muxing for ${state.episodeMetadata}" }
        }
    }

    override fun deleteEpisodeCache(state: CacheEpisodeState) {
        applicationScope.launch {
            try {
                // todo 下载记录也要删除
                logger.info { "Attempting to delete media cache metadata for episode: ${state.episodeMetadata}" }
                val metadataSuccess = state.mediaCacheMetadata.delete()
                if (metadataSuccess) {
                    logger.info { "Media cache metadata deleted successfully." }

                    logger.info { "Attempting to delete media cache storage for episode: ${state.episodeMetadata}" }
                    val storageDeleted = mediaCacheStorage.delete(state.episodeMetadata)
                    if (!storageDeleted) {
                        logger.warn { "Failed to delete from media cache storage, but metadata might be deleted." }
                        return@launch
                    }
                    logger.info { "Media cache storage deleted successfully." }
                } else {
                    logger.warn { "Failed to delete media cache metadata." }
                }
            } catch (e: Exception) {
                logger.error(e) { "Error deleting cache for episode: ${state.episodeMetadata}" }
            }
        }
    }
}