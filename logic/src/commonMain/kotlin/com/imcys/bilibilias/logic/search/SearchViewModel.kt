package com.imcys.bilibilias.logic.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.imcys.bilibilias.logic.stateInViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchViewModel(
//    searchText: String?,
    private val savedStateHandle: SavedStateHandle,
    private val applicationScope: CoroutineScope,
    private val httpDownloader: HttpDownloader,
    private val mediaCacheStorage: MediaCacheDataSource,
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val mediaSourceSelectedUseCase: MediaSourceSelectedUseCase,
    private val preferences: AsPreferencesDataSource,
    private val api: BilibiliLoginApi,
    private val cookieJar: CookieJarDataSource,
) : ViewModel() {
    val selfInfoUiState = preferences.userData
        .map { preferences ->
            viewModelScope
            preferences.selfInfo?.let { SelfInfoUiState.Success(it) } ?: SelfInfoUiState.Guest
        }
        .stateInViewModelScope(SelfInfoUiState.Loading)
    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    val searchResultUiState: StateFlow<SearchResultUiState> =
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
        }.stateInViewModelScope(SearchResultUiState.Loading)

    fun onSearchTriggered(query: String) {}

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onLogout() {
        applicationScope.launch {
            api.exit()
            preferences.setSelfInfo(null)
            cookieJar.clearCookies()
        }
    }

    fun requestCache(request: EpisodeCacheRequest) {
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
}

private const val SEARCH_QUERY = "searchQuery"