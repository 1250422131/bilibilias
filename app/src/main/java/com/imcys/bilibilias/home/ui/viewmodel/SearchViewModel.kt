package com.imcys.bilibilias.home.ui.viewmodel

import android.R.attr.text
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.base.network.NetworkService
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.asResult
import com.imcys.bilibilias.common.base.utils.AsRegexUtil
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.common.base.utils.TextType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")

    @OptIn(FlowPreview::class)
    val searchResultUiState: StateFlow<SearchResultUiState> = searchQuery
        .filter { it.isNotEmpty() }
        .debounce(300)
        .flatMapLatest { parser(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchResultUiState.SearchNotReady,
        )

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun onSearchTriggered(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
//            recentSearchRepository.insertOrReplaceRecentSearch(searchQuery = query)
        }
    }

    private suspend fun parser(text: String): Flow<SearchResultUiState> =
        when (val result = AsRegexUtil.parse(text)) {
            is TextType.AV -> video(NewVideoNumConversionUtils.av2bv(result.text))
            is TextType.BV -> video(result.text) // networkService.n26(bvid)
            is TextType.EP -> bangumi(result.text) // networkService.getBangumiSeasonBeanByEpid(epId)
//        is TextType.ShortLink -> shortLink(result.text) // networkService.shortLink(url)
//        is TextType.UserSpace -> loadUserCardData(result.text) // networkService.getUserCardData(inputString.toLong())
            else -> {
                if (result is TextType.ShortLink) {
                    shortLink(result.text)
                    flowOf(SearchResultUiState.Loading)
                } else {
                    flowOf(SearchResultUiState.LoadFailed("解析失败"))
                }
            }
        }

    private suspend fun userSpace(mid: Long) {
        flowOf(mid)
            .map { networkService.getUserCardData(mid) }
            .asResult()
            .map { result ->
                when (result) {
                    is Result.Error -> SearchResultUiState.LoadFailed(result.exception?.message)
                    Result.Loading -> SearchResultUiState.Loading
                    is Result.Success -> {}
                }
            }
    }

    private suspend fun shortLink(url: String) {
        runCatching { networkService.shortLink(url) }
            .onSuccess { onSearchQueryChanged(it) }
    }

    private suspend fun bangumi(epid: Long): Flow<SearchResultUiState> = flowOf(epid)
        .map { networkService.getBangumiSeasonBeanByEpid(epid).result }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed(result.exception?.message)
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> {
                    val data = result.data
                    SearchResultUiState.Success(
                        bvid = data.episodes.first().bvid,
                        title = data.title,
                        cover = data.episodes.first().cover,
                        ownerName = null,
                    )
                }
            }
        }

    private suspend fun video(bvid: String): Flow<SearchResultUiState> = flowOf(bvid)
        .map { networkService.getVideoBaseInfoByBvid(bvid).data }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed(result.exception?.message)
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> {
                    val data = result.data
                    SearchResultUiState.Success(
                        bvid = data.bvid,
                        title = data.title,
                        cover = data.pic,
                        ownerName = data.owner.name,
                    )
                }
            }
        }
}

sealed interface SearchResultUiState {
    data object Loading : SearchResultUiState

    data class LoadFailed(val message: String?) : SearchResultUiState

    data class Success(
        val bvid: String,
        val title: String,
        val cover: String,
        val ownerName: String?,
    ) : SearchResultUiState

    /**
     * A state where the search contents are not ready. This happens when the *Fts tables are not
     * populated yet.
     */
    data object SearchNotReady : SearchResultUiState
}

private const val SEARCH_QUERY = "searchQuery"