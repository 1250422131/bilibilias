package com.imcys.bilibilias.tool

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.common.utils.InputParsingUtils
import com.imcys.common.utils.Result
import com.imcys.common.utils.asResult
import com.imcys.common.utils.digitalConversion
import com.imcys.model.VideoDetails
import com.imcys.network.repository.video.IVideoDataSources
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
    private val videoRepository: IVideoDataSources,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    val searchResultUiState: StateFlow<SearchResultUiState> = searchQuery
        .flatMapLatest {
            if (it.length < SEARCH_QUERY_MIN_LENGTH) {
                flowOf(SearchResultUiState.EmptyQuery)
            } else {
                when (val type = InputParsingUtils.searchType(it)) {
                    is InputParsingUtils.SearchType.AV -> handleAV(type.id).mapToUserSearchResult()
                    is InputParsingUtils.SearchType.BV -> handleBV(type.id).mapToUserSearchResult()

                    is InputParsingUtils.SearchType.EP -> handleEP(type.id)
                    is InputParsingUtils.SearchType.ShortLink -> {
                        val fullUrl = handleShortLink(type.url)
                        val bvid = InputParsingUtils.getBvid(fullUrl)
                        if (bvid == null) flowOf(SearchResultUiState.LoadFailed)
                        else handleBV(bvid).mapToUserSearchResult()
                    }

                    InputParsingUtils.SearchType.None -> flowOf(SearchResultUiState.LoadFailed)
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

    private suspend fun handleEP(id: String) =
        flowOf(videoRepository.getPgcViewSeason(id))
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> SearchResultUiState.LoadFailed
                    Result.Loading -> SearchResultUiState.Loading
                    is Result.Success -> SearchResultUiState.Success(
                        pic = result.data.cover,
                        title = result.data.title,
                        desc = result.data.evaluate,
                        view = result.data.stat.view.digitalConversion(),
                        danmaku = result.data.stat.danmaku.digitalConversion(),
                        aid = result.data.episodes.first().aid,
                        bvid = result.data.episodes.first().bvid,
                        cid = result.data.episodes.first().cid,
                    )
                }
            }

    private suspend fun handleShortLink(url: String) = videoRepository.shortLink(url)

    private suspend fun handleAV(id: String): Flow<VideoDetails> =
        flowOf(videoRepository.getDetail(id.toLong()))

    private suspend fun handleBV(id: String): Flow<VideoDetails> =
        flowOf(videoRepository.getDetail(id))

    fun Flow<VideoDetails>.mapToUserSearchResult(): Flow<SearchResultUiState> =
        this.asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> SearchResultUiState.LoadFailed
                    Result.Loading -> SearchResultUiState.Loading
                    is Result.Success -> SearchResultUiState.Success(
                        pic = result.data.pic,
                        title = result.data.title,
                        desc = result.data.descV2?.firstOrNull()?.rawText ?: result.data.desc,
                        view = result.data.stat.view.digitalConversion(),
                        danmaku = result.data.stat.danmaku.digitalConversion(),
                        aid = result.data.aid,
                        bvid = result.data.bvid,
                        cid = result.data.cid,
                    )
                }
            }
}

private const val SEARCH_QUERY = "searchQuery"

/**
 * Minimum length where search query is considered as
 * [SearchResultUiState.EmptyQuery]
 */
private const val SEARCH_QUERY_MIN_LENGTH = 2
