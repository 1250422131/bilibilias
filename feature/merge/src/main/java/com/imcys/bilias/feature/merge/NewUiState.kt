package com.imcys.bilias.feature.merge

import com.imcys.model.download.Entry
import kotlinx.collections.immutable.ImmutableList

sealed interface NewUiState {
    data object Empty : NewUiState
    data object Loading : NewUiState
    data class Success(val entries: ImmutableList<Entry>) : NewUiState
}