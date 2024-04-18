package com.imcys.bilibilias.feature.home

import com.imcys.bilibilias.core.model.bilibilias.HomeBanner
import com.imcys.bilibilias.core.model.bilibilias.UpdateNotice

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Empty : HomeUiState
    data class Success(val updateNotice: UpdateNotice, val homeBanner: HomeBanner) : HomeUiState
}
