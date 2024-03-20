package com.imcys.bilibilias.home.ui.fragment.tool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imcys.bilibilias.base.network.NetworkService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolViewModel @Inject constructor(
    private val networkService: NetworkService
) : ViewModel() {
    private val _state = MutableStateFlow(ToolState())
    val uiState = _state.asStateFlow()
    fun getOldToolItem() {
        viewModelScope.launch {
            val toolItem = networkService.getOldToolItem()
            _state.update { state ->
                state.copy(items = toolItem.mapToToolItems().sortedBy { it.toolCode })
            }
        }
    }

    fun getView(bvId: String) {
        viewModelScope.launch {
            val videoInfo = networkService.getVideoBaseInfoByBvid(bvId)
            _state.update { state ->
                state.copy(videoInfo = videoInfo)
            }
        }
    }
}