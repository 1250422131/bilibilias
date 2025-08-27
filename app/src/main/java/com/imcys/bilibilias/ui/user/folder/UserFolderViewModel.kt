package com.imcys.bilibilias.ui.user.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.PagingData
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.network.ApiStatus
import com.imcys.bilibilias.network.NetWorkResult
import com.imcys.bilibilias.network.emptyNetWorkResult
import com.imcys.bilibilias.network.model.user.BILIUserFolderListInfo
import com.imcys.bilibilias.ui.user.source.FolderFavPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class UserFolderViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {
    data class UIState(
        val mid: Long = 0L,
        val currentMediaId: Long = 0L
    )

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private val _folderList =
        MutableStateFlow<NetWorkResult<BILIUserFolderListInfo?>>(emptyNetWorkResult())
    val folderList = _folderList.asStateFlow()

    val items = _uiState
        .flatMapLatest { state ->
            if (state.currentMediaId > 0L) {
                Pager(
                    PagingConfig(pageSize = 40, enablePlaceholders = false)
                ) { FolderFavPagingSource(userInfoRepository, state.currentMediaId) }.flow
            } else {
                flowOf(PagingData.empty())
            }
        }
        .cachedIn(viewModelScope)

    private fun initFolderList() {
        viewModelScope.launch {
            userInfoRepository.getFolderList(_uiState.value.mid).collect {
                _folderList.value = it
                if (it.status == ApiStatus.SUCCESS){
                    it.data?.list?.firstOrNull()?.let { folder ->
                        updateCurrentMediaId(folder.id)
                    }
                }
            }
        }
    }

    fun updateCurrentMediaId(mediaId: Long) {
        if (mediaId != _uiState.value.currentMediaId) {
            _uiState.value = _uiState.value.copy(currentMediaId = mediaId)
        }
    }

    fun initMid(mid: Long) {
        if (mid != _uiState.value.mid) {
            _uiState.value = _uiState.value.copy(mid = mid, currentMediaId = 0L)
            initFolderList()
        }
    }

}