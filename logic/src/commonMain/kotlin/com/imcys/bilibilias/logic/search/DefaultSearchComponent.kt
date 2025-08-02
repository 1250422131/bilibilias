package com.imcys.bilibilias.logic.search

import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.imcys.bilibilias.core.datasource.persistent.TokenPersistent
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.datastore.model.EpisodeMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCachePartMetadata
import com.imcys.bilibilias.core.domain.GetEpisodeInfoUseCase
import com.imcys.bilibilias.core.domain.MediaSourceSelectedUseCase
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeCacheState
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.model.EpisodeInfo
import com.imcys.bilibilias.core.result.Result.Error
import com.imcys.bilibilias.core.result.Result.Loading
import com.imcys.bilibilias.core.result.Result.Success
import com.imcys.bilibilias.core.result.asResult
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultSearchComponent(
    componentContext: AppComponentContext,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheStorage: MediaCacheDataSource,
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val mediaSourceSelectedUseCase: MediaSourceSelectedUseCase,
    private val tokenPersistent: TokenPersistent,
    private val preferences: AsPreferencesDataSource,
) : SearchComponent, AppComponentContext by componentContext {
    @OptIn(ExperimentalStateKeeperApi::class)
    private var persistentState: State by saveable(serializer = State.serializer(), init = ::State)
    override val selfInfoUiState = preferences.userData
        .map { preferences ->
            preferences.selfInfo?.let { SelfInfoUiState.Success(it) } ?: SelfInfoUiState.Guest
        }
        .stateInBackground(SelfInfoUiState.Loading)
    override val searchQuery = MutableStateFlow(persistentState.searchQuery)

    override val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                getEpisodeInfoUseCase(query)
                    .asResult()
                    .map { result ->
                        when (result) {
                            is Success -> {
                                val data = result.data
                                SearchResultUiState.Success(
                                    episodeCacheListState = data,
                                    episodeInfo = data.episodeInfo,
                                    episodes = data.episodes,
                                )
                            }

                            is Error -> SearchResultUiState.Error(
                                result.exception.message ?: "Unknown error"
                            )

                            is Loading -> SearchResultUiState.Loading
                        }
                    }
            }
        }.stateInBackground(SearchResultUiState.Loading)

    override fun onSearchTriggered(query: String) {}

    override fun onSearchQueryChanged(query: String) {
        persistentState = State(query)
        searchQuery.value = query
    }

    override fun requestCache(episode: EpisodeCacheState, request: EpisodeCacheRequest) {
        applicationScope.launch {
            val episodeInfo = mediaSourceSelectedUseCase(request)
            val metadata = episodeInfo.asEpisodeMetadata()
            launch {
                mediaCacheStorage.cacheEpisodeMetadata(metadata)
            }
            launch {
                val videoDownloadState = download(episodeInfo.video.first().baseUrl)
                if (videoDownloadState != null) {
                    cachePartMetadata(metadata, videoDownloadState.downloadId)
                }
            }
            launch {
                val audioDownloadState = download(episodeInfo.audio.first().baseUrl)
                if (audioDownloadState != null) {
                    cachePartMetadata(metadata, audioDownloadState.downloadId)
                }
            }
        }
    }

    suspend fun cachePartMetadata(metadata: EpisodeMetadata, downloadId: DownloadId) {
        mediaCacheStorage.updateMediaCacheMetadata(
            metadata,
            MediaCachePartMetadata(downloadId.value)
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun download(downloadUrl: String): DownloadState? {
        val downloadId = DownloadId(Uuid.random().toString())
        val downloadState = httpDownloader.downloadWithId(downloadId, downloadUrl)
        return downloadState
    }

    fun EpisodeInfo.asEpisodeMetadata(): EpisodeMetadata {
        return EpisodeMetadata(
            bvid = bvid,
            cid = cid,
            title = title
        )
    }

    @Serializable
    private data class State(val searchQuery: String = "")
}
