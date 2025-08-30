package com.imcys.bilibilias.logic.search

import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.imcys.bilibilias.core.datasource.api.BilibiliLoginApi
import com.imcys.bilibilias.core.datastore.AsPreferencesDataSource
import com.imcys.bilibilias.core.datastore.CookieJarDataSource
import com.imcys.bilibilias.core.datastore.MediaCacheDataSource
import com.imcys.bilibilias.core.datastore.model.EpisodeMetadata
import com.imcys.bilibilias.core.datastore.model.MediaCachePartMetadata
import com.imcys.bilibilias.core.domain.GetEpisodeInfoUseCase
import com.imcys.bilibilias.core.domain.MediaSourceSelectedUseCase
import com.imcys.bilibilias.core.domain.model.EpisodeCacheRequest
import com.imcys.bilibilias.core.domain.model.EpisodeInfo
import com.imcys.bilibilias.core.http.downloader.HttpDownloader
import com.imcys.bilibilias.core.http.downloader.model.DownloadId
import com.imcys.bilibilias.core.result.Result.Error
import com.imcys.bilibilias.core.result.Result.Loading
import com.imcys.bilibilias.core.result.Result.Success
import com.imcys.bilibilias.core.result.asResult
import com.imcys.bilibilias.logic.root.AppComponentContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.inject

class DefaultSearchComponent(
    componentContext: AppComponentContext,
    searchText: String?,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheStorage: MediaCacheDataSource,
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val mediaSourceSelectedUseCase: MediaSourceSelectedUseCase,
    private val preferences: AsPreferencesDataSource,
) : SearchComponent, AppComponentContext by componentContext {
    @OptIn(ExperimentalStateKeeperApi::class)
    private var persistentState: State by saveable(serializer = State.serializer(), init = ::State)
    override val selfInfoUiState = preferences.userData
        .map { preferences ->
            preferences.selfInfo?.let { SelfInfoUiState.Success(it) } ?: SelfInfoUiState.Guest
        }
        .stateInBackground(SelfInfoUiState.Loading)
    override val searchQuery = MutableStateFlow(searchText ?: persistentState.searchQuery)

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
                                if (data == null) {
                                    SearchResultUiState.Error("解析失败")
                                } else {
                                    SearchResultUiState.Success(
                                        episodeCacheListState = data,
                                        episodeInfo = data.episodeInfo,
                                        episodes = data.episodes,
                                    )
                                }
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

    override fun onLogout() {
        val api: BilibiliLoginApi by inject()
        val cookieJar: CookieJarDataSource by inject()

        applicationScope.launch {
            api.exit()
            preferences.setSelfInfo(null)
            cookieJar.clearCookies()
        }
    }

    override fun requestCache(request: EpisodeCacheRequest) {
        applicationScope.launch {
            val episodeInfo = mediaSourceSelectedUseCase(request)
            val metadata = episodeInfo.asEpisodeMetadata()

            mediaCacheStorage.cacheEpisodeMetadata(metadata)
            episodeInfo.urls.map {
                async {
                    val downloadId = httpDownloader.download(it.backupUrl.random().url)
                    cachePartMetadata(metadata, downloadId)
                }
            }.awaitAll()
        }
    }

    suspend fun cachePartMetadata(metadata: EpisodeMetadata, downloadId: DownloadId) {
        mediaCacheStorage.updateMediaCacheMetadata(
            metadata,
            MediaCachePartMetadata(downloadId.value)
        )
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
