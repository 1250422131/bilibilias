package com.imcys.bilibilias.ui.user.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imcys.bilibilias.data.repository.UserInfoRepository
import com.imcys.bilibilias.ui.user.source.HistoryPlayPagingSource

class UserPlayHistoryViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    val items = Pager(
        PagingConfig(pageSize = 20, enablePlaceholders = false)
    ) { HistoryPlayPagingSource(userInfoRepository) }.flow
        .cachedIn(viewModelScope)


}