package com.imcys.bilibilias.ui.user.bangumifollow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.ui.user.source.BangumiFollowPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class BangumiFollowViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    data class UIState(
        val mid: Long = 0L,
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()


    val items = _uiState.flatMapLatest { state ->
        Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = true)
        ) { BangumiFollowPagingSource(userInfoRepository, state.mid) }.flow.cachedIn(viewModelScope)
    }

    fun initMid(mid: Long) {
        if (mid != _uiState.value.mid) _uiState.value = _uiState.value.copy(mid = mid)
    }

}