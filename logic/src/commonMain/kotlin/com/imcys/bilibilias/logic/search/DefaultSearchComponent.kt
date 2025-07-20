package com.imcys.bilibilias.logic.search

import co.touchlab.kermit.Logger
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.imcys.bilibilias.core.data.DataStoreProvider
import com.imcys.bilibilias.core.data.GetEpisodeInfoUseCase
import com.imcys.bilibilias.core.data.MediaSourceSelectedUseCase
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.http.downloader.model.DownloadState
import com.imcys.bilibilias.core.media.cache.EpisodeMetadata
import com.imcys.bilibilias.core.media.cache.MediaCachePartMetadata
import com.imcys.bilibilias.core.media.cache.mediaCacheMetadata
import com.imcys.bilibilias.core.model.EpisodeInfo
import com.imcys.bilibilias.core.result.Result.Error
import com.imcys.bilibilias.core.result.Result.Loading
import com.imcys.bilibilias.core.result.Result.Success
import com.imcys.bilibilias.core.result.asResult
import com.imcys.bilibilias.logic.utils.scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultSearchComponent(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {
    private val httpDownloader = DataStoreProvider.httpDownloader
    private val mediaCacheStorage = DataStoreProvider.mediaCacheStorage
    private val episodeInfoUseCase = GetEpisodeInfoUseCase()
    private val mediaSourceSelectedUseCase = MediaSourceSelectedUseCase()

    @OptIn(ExperimentalStateKeeperApi::class)
    private var state: State by saveable(serializer = State.serializer(), init = ::State)

    init {
        scope.launch {
            httpDownloader.init()
        }
    }

    override val searchQuery = MutableStateFlow(state.searchQuery)

    // use case
    override val searchResultUiState: StateFlow<SearchResultUiState> =
        searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                episodeInfoUseCase.create(query).asResult().map { result ->
                    when (result) {
                        is Success -> {
                            val data = result.data
                            if (data != null) {
                                SearchResultUiState.Success(data)
                            } else {
                                SearchResultUiState.Error("No data")
                            }
                        }

                        is Error -> SearchResultUiState.Error(
                            result.exception.message ?: "Unknown error"
                        )

                        is Loading -> SearchResultUiState.Loading
                    }
                }
            }
        }.stateIn(
            scope,
            SharingStarted.WhileSubscribed(5_000),
            SearchResultUiState.Loading
        )

    override fun onSearchTriggered(query: String) {}

    override fun onSearchQueryChanged(query: String) {
        state = State(query)
        searchQuery.update { query }
    }

    override fun downloadItem(qn: Int, bvid: String, cid: Long) {
        Logger.i { "downloadItem: $qn, $bvid, $cid" }
        scope.launch {
            val episodeInfo = mediaSourceSelectedUseCase(qn, bvid, cid)
            val videoDownloadState = download(episodeInfo.video.first().baseUrl)
            val audioDownloadState = download(episodeInfo.audio.first().baseUrl)
            if (videoDownloadState != null && audioDownloadState != null) {
                mediaCacheStorage.cache(
                    episodeInfo.asEpisodeMetadata(),
                    mediaCacheMetadata(
                        MediaCachePartMetadata(videoDownloadState.downloadId.value),
                        MediaCachePartMetadata(audioDownloadState.downloadId.value)
                    )
                )
            } else {

            }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun download(downloadUrl: String): DownloadState? {
        val downloadId = DownloadId(Uuid.random().toString())
        Logger.i { "Creating download recorder - URL: $downloadUrl, ID: $downloadId" }
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

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Main : Config

        @Serializable
        data object Login : Config
    }
}
