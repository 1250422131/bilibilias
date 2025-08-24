package com.imcys.bilibilias.ui.user.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.data.model.BILISpaceArchiveModel
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.network.ApiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(kotlinx.coroutines.FlowPreview::class)
class WorkListViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    data class UIState(
        val mid: Long = 0L,
        val page: Int = 0,
        val query: String = "",
        val items: List<BILISpaceArchiveModel.Item> = emptyList(),
        val hasMore: Boolean = true,
        val isRefreshing: Boolean = false,
        val isAppending: Boolean = false,
        val errorMsg: String? = null,
    ) {
        val isEmpty: Boolean get() = items.isEmpty()
    }

    private val PAGE_SIZE = 20

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val queryFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            queryFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { _ ->
                    loadWorkList(1, false, isClear = true)
                }
        }
    }

    fun init(mid: Long) {
        if (mid != _uiState.value.mid) {
            _uiState.update { it.copy(mid = mid) }
            refreshWorkList()
        }
    }

    fun onQueryChange(keyword: String?) {
        val q = keyword.orEmpty()
        _uiState.update { it.copy(query = q) }
        queryFlow.tryEmit(q)
    }

    fun refreshWorkList() {
        val mid = _uiState.value.mid
        if (mid == 0L) return
        if (_uiState.value.isRefreshing) return
        loadWorkList(page = 1, isRefresh = true)
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (state.isRefreshing || state.isAppending || !state.hasMore) return
        loadWorkList(page = state.page + 1, isRefresh = false)
    }

    private fun loadWorkList(page: Int, isRefresh: Boolean, isClear: Boolean = false) {
        val current = _uiState.value
        if (isRefresh) {
            _uiState.update {
                it.copy(
                    isRefreshing = true,
                    isAppending = false,
                    errorMsg = null,
                    items = if (page == 1) emptyList() else it.items,
                    hasMore = true
                )
            }
        } else {
            _uiState.update { it.copy(isAppending = true, errorMsg = null) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            userInfoRepository
                .getSpaceArchiveInfo(
                    mid = current.mid,
                    pn = page,
                    ps = PAGE_SIZE,
                    keyword = current.query.ifBlank { null }
                )
                .collect { result ->
                    when (result.status) {
                        ApiStatus.SUCCESS -> {
                            val data = result.data
                            val newList = data?.list ?: emptyList()
                            _uiState.update { s ->
                                val merged =
                                    if (isRefresh || isClear) newList else s.items + newList
                                s.copy(
                                    items = merged,
                                    page = page,
                                    hasMore = data?.page?.hasNext ?: false,
                                    isRefreshing = false,
                                    isAppending = false,
                                    errorMsg = null
                                )
                            }
                        }

                        ApiStatus.ERROR -> {
                            _uiState.update {
                                it.copy(
                                    isRefreshing = false,
                                    isAppending = false,
                                    errorMsg = result.errorMsg
                                )
                            }
                        }

                        else -> Unit
                    }
                }
        }
    }

}