package com.imcys.bilibilias.feature.tool

import DownloadFileRequest
import SearchResultUiState
import View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.core.common.result.Result
import com.imcys.bilibilias.core.common.result.asResult
import com.imcys.bilibilias.core.domain.GetStreamWithBangumiDetailUseCase
import com.imcys.bilibilias.core.domain.GetStreamWithVideoDetailUseCase
import com.imcys.bilibilias.core.network.download.DownloadParameter
import com.imcys.bilibilias.core.network.download.FileDownload
import com.imcys.bilibilias.core.network.repository.VideoRepository
import com.imcys.bilibilias.feature.tool.util.InputParseUtil
import com.imcys.bilibilias.feature.tool.util.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import mapToVideoStreamDesc
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getStreamWithVideoDetailUseCase: GetStreamWithVideoDetailUseCase,
    private val getStreamWithBangumiDetailUseCase: GetStreamWithBangumiDetailUseCase,
    private val fileDownload: FileDownload,
    private val videoRepository: VideoRepository,
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    val searchResultUiState = searchQuery.map {
        InputParseUtil.searchType(it)
    }.flatMapLatest {
        when (it) {
            is SearchType.AV -> handleAV(it.id)
            is SearchType.BV -> handleBV(it.id)
            is SearchType.EP -> handleEP(it.id)
            is SearchType.ShortLink -> handleShortLink(it.url)
            SearchType.None -> flowOf(SearchResultUiState.EmptyQuery)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SearchResultUiState.Loading)

    fun download(request: DownloadFileRequest) {
        fileDownload.enqueue(
            DownloadParameter(
                request.aid,
                request.bvid,
                request.cid,
                request.quality
            )
        )
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun clearSearches() {
        savedStateHandle[SEARCH_QUERY] = ""
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
}

private const val SEARCH_QUERY = "searchQuery"
