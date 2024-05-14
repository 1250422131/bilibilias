package com.imcys.bilibilias.feature.tool

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.decompose.value.update
import com.imcys.bilibilias.core.common.result.Result
import com.imcys.bilibilias.core.common.result.asResult
import com.imcys.bilibilias.core.common.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.core.domain.GetStreamWithBangumiDetailUseCase
import com.imcys.bilibilias.core.domain.GetStreamWithVideoDetailUseCase
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.feature.tool.util.InputParseUtil
import com.imcys.bilibilias.feature.tool.util.SearchType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.builtins.serializer

class DefaultToolComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val getStreamWithVideoDetailUseCase: GetStreamWithVideoDetailUseCase,
    private val getStreamWithBangumiDetailUseCase: GetStreamWithBangumiDetailUseCase,
    private val downloadManager: DownloadManager,
    private val videoRepository: VideoRepository,
) : ToolComponent, ComponentContext by componentContext {
    private val _searchQuery = MutableStateFlow("")
    override val searchQuery = _searchQuery.asStateFlow()

    override val searchResultUiState = searchQuery.map {
        InputParseUtil.searchType(it)
    }.flatMapLatest {
        when (it) {
            is SearchType.AV -> handleAV(it.id)
            is SearchType.BV -> handleBV(it.id)
            is SearchType.EP -> handleEP(it.id)
            is SearchType.ShortLink -> handleShortLink(it.url)
            SearchType.None -> flowOf(SearchResultUiState.EmptyQuery)
        }
    }.stateIn(
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
        SharingStarted.Eagerly,
        SearchResultUiState.Loading
    )

    override fun download(request: DownloadRequest) {

        downloadManager.download(request)
    }

    override fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    override fun clearSearches() {
        _searchQuery.update { "" }
    }

    private suspend fun handleShortLink(url: String): Flow<SearchResultUiState> {
        return flowOf(videoRepository.shortLink(url)).map {
            InputParseUtil.searchType(it)
        }.flatMapLatest { type ->
            when (type) {
                is SearchType.AV -> handleAV(type.id)
                is SearchType.BV -> handleBV(type.id)
                is SearchType.EP -> handleEP(type.id)
                SearchType.None -> flowOf(SearchResultUiState.EmptyQuery)
                is SearchType.ShortLink -> flowOf(SearchResultUiState.EmptyQuery)
            }
        }
    }

    private fun handleAV(id: String): Flow<SearchResultUiState> {
        val bvid = NewVideoNumConversionUtils.av2bv(id.toLong())
        return handleBV(bvid)
    }

    private fun handleBV(id: String): Flow<SearchResultUiState> {
        return getStreamWithVideoDetailUseCase(id).asResult().map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> {
                    val (detail, streams) = result.data
                    val descs = streams.map {
                        it.streamUrl.mapToVideoStreamDesc()
                    }
                    val collection = detail.pages.zip(descs).map {
                        View(it.first.cid, it.first.part, it.second)
                    }
                    SearchResultUiState.Success(detail.aid, detail.bvid, detail.cid, collection)
                }
            }
        }
    }

    private fun handleEP(id: String): Flow<SearchResultUiState> {
        return getStreamWithBangumiDetailUseCase(id.toLong()).asResult().map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> {
                    val (detail, streams) = result.data
                    val descs = streams.map {
                        it.streamUrl.mapToVideoStreamDesc()
                    }
                    val collection = detail.episodes.zip(descs).map {
                        View(it.first.cid, it.first.longTitle, it.second)
                    }
                    val episode = detail.episodes.first()
                    SearchResultUiState.Success(episode.aid, episode.bvid, episode.cid, collection)
                }
            }
        }
    }

    @AssistedFactory
    interface Factory : ToolComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): DefaultToolComponent
    }
}
