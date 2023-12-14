package com.imcys.space

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imcys.network.repository.user.GetUserSubmittedVideoPagingSource
import com.imcys.network.repository.user.IUserDataSources
import com.imcys.space.navigation.MID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserSpaceViewModel @Inject constructor(
    private val userRepository: IUserDataSources,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val mid = savedStateHandle.getStateFlow(MID, 0L)
    val spaceArcSearchPagingSource = Pager(
        PagingConfig(pageSize = 20),
    ) {
        GetUserSubmittedVideoPagingSource(userRepository, mid.value)
    }
        .flow
        .cachedIn(viewModelScope)
}


