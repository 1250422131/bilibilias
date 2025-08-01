package com.imcys.bilibilias.logic.search

import com.imcys.bilibilias.core.datastore.model.SelfInfo

sealed interface SelfInfoUiState {
    data object Loading : SelfInfoUiState
    data object Guest : SelfInfoUiState
    data class Success(val selfInfo: SelfInfo) : SelfInfoUiState
}
