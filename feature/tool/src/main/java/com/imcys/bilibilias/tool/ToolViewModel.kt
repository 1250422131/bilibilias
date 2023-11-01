package com.imcys.bilibilias.tool


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.common.utils.AsVideoUtils
import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.common.utils.digitalConversion
import com.imcys.model.Bangumi
import com.imcys.model.VideoDetails
import com.imcys.network.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    val searchResultUiState: StateFlow<SearchResultUiState> = searchQuery
        .flatMapLatest {
            if (it.length < SEARCH_QUERY_MIN_LENGTH) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                when (val type = searchType(it)) {
                    is SearchType.AV -> handleAV(type.id).mapToUserSearchResult()
                    is SearchType.BV -> handleBV(type.id).mapToUserSearchResult()

                    is SearchType.EP -> handleEP(type.id)
                    is SearchType.ShortLink -> {
                        val fullUrl = handleShortLink(type.url)
                        val bvid = AsVideoUtils.getBvid(fullUrl)
                        if (bvid == null) flowOf(SearchResultUiState.LoadFailed)
                        else handleBV(bvid).mapToUserSearchResult()
                    }

                    SearchType.None -> flowOf(SearchResultUiState.LoadFailed)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SearchResultUiState.Loading,
        )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun clearSearches() {
        savedStateHandle[SEARCH_QUERY] = ""
    }

    @Suppress("ReturnCount")
    private fun searchType(text: String): SearchType {
        val bv = AsVideoUtils.getBvid(text)
        if (bv != null) {
            return SearchType.BV(bv)
        }
        val ep = AsVideoUtils.getEpid(text)
        if (ep != null) {
            return SearchType.EP(ep)
        }
        val aid = AsVideoUtils.getAid(text)
        if (aid != null) {
            return SearchType.AV(aid)
        }
        val link = AsVideoUtils.getShortLink(text)
        if (link != null) {
            return SearchType.ShortLink(link)
        }
        return SearchType.None
    }

    private suspend fun handleEP(id: String) =
        flowOf(videoRepository.getEp(id)).mapToUserSearchResult()

    private suspend fun handleShortLink(url: String) = videoRepository.shortLink(url)

    private suspend fun handleAV(id: String): Flow<VideoDetails> =
        flowOf(videoRepository.getVideoDetailsByAid(id))

    private suspend fun handleBV(id: String): Flow<VideoDetails> =
        flowOf(videoRepository.getVideoDetailsByBvid(id))

    fun Flow<Bangumi>.mapToUserSearchResult(): Flow<SearchResultUiState> =
        this.asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> SearchResultUiState.LoadFailed
                    Result.Loading -> SearchResultUiState.Loading
                    is Result.Success -> SearchResultUiState.Success(
                        pic = result.data.result.cover,
                        title = result.data.result.title,
                        desc = result.data.result.evaluate,
                        view = result.data.result.stat.views.digitalConversion(),
                        danmaku = result.data.result.stat.danmakus.digitalConversion()
                    )
                }
            }

    fun Flow<VideoDetails>.mapToUserSearchResult(): Flow<SearchResultUiState> =
        this.asResult().map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> SearchResultUiState.Success(
                    pic = result.data.pic,
                    title = result.data.title,
                    desc = result.data.descV2?.firstOrNull()?.rawText ?: result.data.desc,
                    view = result.data.stat.view.digitalConversion(),
                    danmaku = result.data.stat.danmaku.digitalConversion()
                )
            }
        }
}

private const val TAG = "ToolViewModel"
private const val SEARCH_QUERY = "searchQuery"

/** Minimum length where search query is considered as [SearchResultUiState.EmptyQuery] */
private const val SEARCH_QUERY_MIN_LENGTH = 2
