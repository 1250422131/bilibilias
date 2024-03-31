package com.imcys.bilibilias.home.ui.fragment.tool

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.common.base.extend.Result
import com.imcys.bilibilias.common.base.extend.asResult
import com.imcys.bilibilias.common.base.utils.NewVideoNumConversionUtils
import com.imcys.bilibilias.core.network.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val videoRepository: VideoRepository
) : ViewModel() {
    val searchQuery = savedStateHandle.getStateFlow(key = SEARCH_QUERY, initialValue = "")
    val searchResultUiState = searchQuery.map {
        InputParseUtil.searchType(it)
    }.flatMapLatest {
        when (it) {
            is SearchType.AV -> handleAV(it.id)
            is SearchType.BV -> handleBV(it.id)
            is SearchType.EP -> handleEP(it.id)
            SearchType.None -> flowOf(SearchResultUiState.EmptyQuery)
            is SearchType.ShortLink -> TODO()
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, SearchResultUiState.Loading)

    private fun handleEP(id: String): Flow<SearchResultUiState> {
        TODO("Not yet implemented")
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun clearSearches() {
        savedStateHandle[SEARCH_QUERY] = ""
    }

    private suspend fun handleAV(id: String): Flow<SearchResultUiState> {
        val bvid = NewVideoNumConversionUtils.av2bv(id.toLong())
        return handleBV(bvid)
    }

    private suspend fun handleBV(id: String): Flow<SearchResultUiState> {
        return flowOf(videoRepository.获取视频详细信息(id)).asResult().map { result ->
            when (result) {
                is Result.Error -> SearchResultUiState.LoadFailed
                Result.Loading -> SearchResultUiState.Loading
                is Result.Success -> {
                    val detail = result.data
                    val collection = detail.pages.map { View(it.cid, it.part) }
                    SearchResultUiState.Success(detail.aid, detail.bvid, detail.cid, collection)
                }
            }
        }
    }
}

private const val SEARCH_QUERY = "searchQuery"
