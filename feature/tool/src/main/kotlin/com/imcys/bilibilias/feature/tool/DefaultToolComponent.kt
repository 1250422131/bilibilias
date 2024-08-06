package com.imcys.bilibilias.feature.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.imcys.bilibilias.core.common.result.Result
import com.imcys.bilibilias.core.common.result.asResult
import com.imcys.bilibilias.core.domain.GetStreamWithBangumiDetailUseCase
import com.imcys.bilibilias.core.domain.GetStreamWithVideoDetailUseCase
import com.imcys.bilibilias.core.download.DownloadManager
import com.imcys.bilibilias.core.download.DownloadRequest
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.feature.tool.util.ConversionUtil
import com.imcys.bilibilias.feature.tool.util.InputParseUtil
import com.imcys.bilibilias.feature.tool.util.SearchType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DefaultToolComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val getStreamWithVideoDetailUseCase: GetStreamWithVideoDetailUseCase,
    private val getStreamWithBangumiDetailUseCase: GetStreamWithBangumiDetailUseCase,
    private val downloadManager: DownloadManager,
    private val videoRepository: VideoRepository,
) : ViewModel(),
    ToolComponent,
    ComponentContext by componentContext {

    private val savedState = instanceKeeper.getOrCreate(::SavedState)
    private val _searchQuery = savedState.getStateFlow()

    override val searchQuery = _searchQuery

    override val searchResultUiState = _searchQuery.map {
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
        viewModelScope,
        SharingStarted.Eagerly,
        SearchResultUiState.Loading,
    )

    override fun download(request: DownloadRequest) {
        downloadManager.download(request)
    }

    override fun onSearchQueryChanged(query: String) {
        savedState.set(query)
    }

    override fun clearSearches() {
        savedState.set("")
    }

    private suspend fun handleShortLink(url: String): Flow<SearchResultUiState> = flowOf(videoRepository.shortLink(url)).map {
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

    private fun handleAV(id: String): Flow<SearchResultUiState> {
        val bvid = ConversionUtil.av2bv(id.toLong())
        return handleBV(bvid)
    }

    private fun handleBV(id: String): Flow<SearchResultUiState> = getStreamWithVideoDetailUseCase(id).asResult().map { result ->
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
                SearchResultUiState.Success(
                    detail.aid,
                    detail.bvid,
                    detail.cid,
                    detail.owner.mid,
                    collection,
                    detail.owner.face,
                    detail.owner.mid,
                )
            }
        }
    }

    private fun handleEP(id: String): Flow<SearchResultUiState> = getStreamWithBangumiDetailUseCase(id.toLong()).asResult().map { result ->
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
                SearchResultUiState.Success(
                    episode.aid,
                    episode.bvid,
                    episode.cid,
                    detail.upInfo.mid,
                    collection,
                    detail.upInfo.avatar,
                    detail.upInfo.mid,
                )
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

private class SavedState : InstanceKeeper.Instance {
    private val flows = MutableStateFlow<String>("")
    fun set(value: String) {
        flows.value = value
    }

    fun getStateFlow() = flows.asStateFlow()
}
